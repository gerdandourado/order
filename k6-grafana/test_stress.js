/*

------------------- CENARIO DE TESTE -------------------
Buscando todos os crocodilos

Criterios aplicados:
    - performance onde:
        - carga de 1000 vu por 1m

Validações:
    - tempo requisicao p(95) < 250ms
    - requisicao com falha < 1%
*/

// IMPORTS
import http from 'k6/http';
import { check, sleep } from 'k6';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";


export const options = {
    stages: [
        {
            duration: '1m',
            target: 1000
        },
        {
            duration: '30s',
            target: 0
        }
    ],
    thresholds: {
        http_req_failed: ['rate < 0.01'],
        http_req_duration: ['p(95) < 250']
    }
}

const BASE_URL = 'http://localhost:8081/order';


export default function(){

    const params = {
        headers: {
            Authorization: `Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJIZnpJNldHZkRVeDU0aWFQUFhBQlFmY1FTQlFJYy1uYmlfNjczOGlEOExNIn0.eyJleHAiOjE3MzU5Mjc0NDgsImlhdCI6MTczNTkyNzE0OCwianRpIjoiOTRiMTRlMTUtYjhkMy00YjY4LWI1ZjAtY2MzMmRlZWU3ZDkwIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDkwL3JlYWxtcy9hbWJldiIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI2YThjNDA1Yy02ODc4LTQzMjktYThiYy03N2ZhY2Y4ODQwNTciLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJjbGllbnRlLWV4dGVybm8tYSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiLyoiXSwicmVhbG1fYWNjZXNzIjp7InJvbGVzIjpbIlJPTEVfQ0xJRU5URV9FWFRFUk5PX0EiLCJvZmZsaW5lX2FjY2VzcyIsInVtYV9hdXRob3JpemF0aW9uIiwiZGVmYXVsdC1yb2xlcy1hbWJldiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoicHJvZmlsZSBlbWFpbCIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwiY2xpZW50SG9zdCI6IjE5Mi4xNjguMzIuMSIsInByZWZlcnJlZF91c2VybmFtZSI6InNlcnZpY2UtYWNjb3VudC1jbGllbnRlLWV4dGVybm8tYSIsImNsaWVudEFkZHJlc3MiOiIxOTIuMTY4LjMyLjEiLCJjbGllbnRfaWQiOiJjbGllbnRlLWV4dGVybm8tYSJ9.YCGK0HiKr-xLXOx-DINc0fXhmF4fJxHvT6Hzks3fcbyU5YO0CJRrgHbw0CIP5iTwNpj5IfoqX8MErVYCyoGXUVFzabhXS8l2qwnlBUfMfHlDf--J6gSrqeXR5wASJtfFBpU0xKHuzuR8fB5UDhYyJaghTY0IqfR8zV33RxLXNLuiI2euOy3mUbl5PLS1agxYcRaygLpi6YDzLSo3Xcx9AtubqQd-uoch2vb7AjpfXjRTkNcplGdGDe6dZBJEq6iQjPY4wrw307H0doxiDGjpQWYiVMCEHu3Ng1862H4bsuW0OBbjjG5ANBP-S1eWAUrqmNzePlkb6jlxSpXkHzWUTg` ,
            'Content-Type': 'application/json'
        }
    }
    const res = http.get(`${BASE_URL}/pedidos?page=0&size=100&sortBy=id&data-de-corte=2024-12-31T09:00:00`, params);

    check(res, {
        'status code 200': (r) => r.status === 200
    });

    sleep(1)
}

export function handleSummary(data) {
    return {
        "index.html": htmlReport(data),
    };
}