import React, {useEffect, useRef, useState} from 'react';
import {NavLink, useNavigate, useParams} from "react-router-dom";
import {getLink} from "@/api/linkApi.ts";
import {Link} from "lucide-react";

const RedirectPage: React.FC = () => {
    const navigate = useNavigate();
    const { id } = useParams<{ id: string }>();
    const [countdown, setCountdown] = useState<number>(3);
    const [isDestination, setIsDestination] = useState<boolean>(true);
    const [isRedirecting, setIsRedirecting] = useState<boolean>(true);
    const [error, setError] = useState<string>("");
    const [originalUrl, setOriginalUrl] = useState<string | null>(null);

    useEffect(() => {
        const fetchLink = async () => {
            try {
                const response = await getLink(id!);
                const { url } = response.data;
                setOriginalUrl(url);
                setIsDestination(true);
                setIsRedirecting(true);
            } catch (error) {
                console.error(error);
                setError("Failed to retrieve the original link");
                setIsDestination(false);
                setIsRedirecting(true);
            }
        };

        fetchLink();
    }, [id]);

    useEffect(() => {
        if (!isRedirecting) return;

        let timer;

        if (countdown > 0) {
            timer = setInterval(() => {
                setCountdown(prevCount => {
                    if (prevCount <= 1) {
                        clearInterval(timer);
                        if (isDestination && originalUrl) {
                            window.location.href = originalUrl;
                        } else {
                            navigate("/");
                        }
                    }
                    return prevCount - 1;
                });
            }, 1000);
        }

        return () => {
            if (timer) clearInterval(timer);
        };
    }, [countdown, isRedirecting, isDestination, originalUrl, navigate]);

    return (
        <div className="flex min-h-screen flex-col bg-pattern">
            <main className="flex flex-1 flex-col items-center justify-center px-4">
                <div className="w-full max-w-md border border-black bg-white p-8 text-center">
                    <div className="mb-6 flex items-center justify-center">
                        {/* Logo */}
                        <div className="mb-4 flex items-center justify-center">
                            <Link className="mr-2 h-8 w-8 text-black" />
                            <h1 className="text-5xl font-bold tracking-tight">SHORTIFY</h1>
                        </div>
                    </div>

                    {isRedirecting ? (
                        <>
                            <div className="mb-6 flex justify-center">
                                <div className="h-8 w-8 animate-spin rounded-full border-4 border-black border-t-transparent"></div>
                            </div>
                            <p className="mb-2 text-lg">Redirecting you to {originalUrl ??
                                (isDestination ? "your destination" : "the home page")}</p>
                            <p className="text-sm text-gray-600">You will be redirected in {countdown} seconds...</p>
                        </>
                    ) : (
                        <div className="text-red-600">
                            <p className="text-lg">{error}</p>
                            <a
                                href="/"
                                className="mt-4 inline-block border border-black bg-black px-4 py-2 text-sm text-white hover:bg-gray-800"
                            >
                                Back to Shortify
                            </a>
                        </div>
                    )}
                </div>
            </main>
        </div>
    )
};

export default RedirectPage;
