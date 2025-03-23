import axios from "axios";
import {CreateLinkRequest, LinkResponse} from "@/@types/link";
import { LINK_API_URL as BASE_URL } from "../../config.ts";

export const createLink = (request: CreateLinkRequest) =>
    axios.post<CreateLinkRequest, { data: LinkResponse }>(`${BASE_URL}/`, request);

export const getLink = (id: string) =>
    axios.get<string, { data: LinkResponse }>(`${BASE_URL}/${id}`);