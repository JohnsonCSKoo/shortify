import React, {useState} from 'react';
import {CreateLinkRequest} from "@/@types/link";
import {createLink} from "@/api/linkApi.ts";
import {Link} from "lucide-react";

const MainPage: React.FC = () => {
    const [url, setUrl] = useState<string>("");
    const [shortUrl, setShortUrl] = useState<string>("");
    const [isLoading, setIsLoading] = useState<boolean>(false);
    const [error, setError] = useState<string>("");

    const validateAndSubmitUrl = async () => {
        setError("");
        setShortUrl("");

        // URL validation
        if (!url) {
            setError("Please enter a URL");
            return;
        }

        let cleanUrl = url;
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            cleanUrl = `https://${url}`;
        }

        try {
            new URL(cleanUrl);
        } catch (error) {
            console.error(error);
            setError("Please enter a valid URL");
            return;
        }

        setIsLoading(true);

        const request: CreateLinkRequest = {
            url: cleanUrl,
        }

        await createLink(request)
            .then((response) => {
                const { id } = response.data;
                const currentUrl = window.location.origin;
                setShortUrl(`${currentUrl}/${id}`);
            }).catch((error) => {
                console.error(error);
                setError("An unexpected error occurred. Please try again.");
            }).finally(() => {
                setIsLoading(false);
            });
    }

    const copyToClipboard = () => {
        navigator.clipboard
            .writeText(shortUrl)
            .then(() => {
                // Show a temporary "Copied!" message
                const button = document.getElementById("copy-button")
                if (button) {
                    const originalText = button.textContent
                    button.textContent = "Copied!"
                    setTimeout(() => {
                        button.textContent = originalText
                    }, 2000)
                }
            })
            .catch((err) => {
                console.error("Failed to copy: ", err)
            })
    }

    return (
        <div className="flex min-h-screen flex-col bg-pattern">
            {/* Main Content */}
            <main className="flex flex-1 flex-col items-center justify-center px-4 py-16">
                <div className="container mx-auto max-w-4xl">
                    <div className="mb-12 text-center">
                        {/* Logo */}
                        <div className="mb-4 flex items-center justify-center">
                            <Link className="mr-2 h-8 w-8 text-black" />
                            <h1 className="text-5xl font-bold tracking-tight">SHORTIFY</h1>
                        </div>
                        <p className="mx-auto mb-12 max-w-2xl text-xl">
                            Create short, memorable links for your content.
                        </p>
                    </div>

                    <div className="mb-8 border border-black bg-white p-6">
                        <div className="mb-4 flex flex-col gap-2 md:flex-row">
                            <input
                                type="text"
                                placeholder="Enter your URL"
                                value={url}
                                onChange={(e) => setUrl(e.target.value)}
                                className="flex-1 border border-black bg-white px-3 py-2 focus:outline-none"
                            />
                            <button
                                onClick={validateAndSubmitUrl}
                                disabled={isLoading}
                                className="bg-black px-4 py-2 font-medium text-white hover:bg-gray-800 disabled:opacity-50"
                            >
                                {isLoading ? "Processing..." : "Shorten URL"}
                            </button>
                        </div>

                        {error && <p className="mb-4 text-sm text-red-600">{error}</p>}

                        {shortUrl && (
                            <div className="mt-6 border border-black bg-[#e8f4a0] p-4">
                                <p className="mb-2 text-sm font-medium text-black">Your shortened URL:</p>
                                <div className="flex items-center gap-2">
                                    <code className="flex-1 overflow-x-auto border border-black bg-white p-2 text-sm text-black">
                                        {shortUrl}
                                    </code>
                                    <button
                                        id="copy-button"
                                        onClick={copyToClipboard}
                                        className="bg-black px-3 py-1 text-sm text-white hover:bg-gray-800"
                                    >
                                        Copy
                                    </button>
                                </div>
                            </div>
                        )}
                    </div>
                </div>
            </main>
        </div>
    )
};

export default MainPage;
