import http from 'k6/http';
import { sleep, check } from 'k6';

export const options = {
    vus: 500, // 동시에 요청을 보낼 가상 유저 수
    duration: '30s', // 테스트 지속 시간
    thresholds: {
        http_req_duration: ['p(99)<500'], // 99% 응답시간이 500ms 이하
    },
};

// 실제 발급받은 JWT 토큰으로 대체하세요
const TOKEN = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3NDg4OTE0MDYsImV4cCI6MzI4NTI4NTkwMDYsInN1YiI6IkFjY2Vzc1Rva2VuIiwiaWQiOiI2ODNkZjcwZTc2ZDVlZDQ0YTY3ODFiZGEiLCJyb2xlIjoiUk9MRV9VU0VSIn0.TvQo3WumB2YEpsCFxvCH1UdEInjQ9y55803BPys5HTc';

export default function () {
    const headers = {
        headers: {
            Authorization: `Bearer ${TOKEN}`,
            'Content-Type': 'application/json',
        },
    };

    const res = http.get('http://localhost:8080/api/board?page=0', headers);

    check(res, {
        'status is 200': (r) => r.status === 200,
    });

    sleep(1);
}
