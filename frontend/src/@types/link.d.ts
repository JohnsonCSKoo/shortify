export interface LinkResponse {
    id: string;
    url: string;
}

export interface CreateLinkRequest {
    url: string;
}

export interface GetLinkRequest {
    id: string;
}