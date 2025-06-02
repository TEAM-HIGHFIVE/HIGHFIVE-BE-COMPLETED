package advancedweb.project.datacollector.domain.service;

import advancedweb.project.datacollector.application.dto.response.crawling.CrawlingResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WelfareCrawlingService {

    private final ObjectMapper objectMapper;

    public CrawlingResponse crawl(String link) {
        try {
            Document doc = Jsoup.connect(link).get();

            // <script> 태그 중 initParameter 호출 포함된 것 찾기
            Elements scripts = doc.select("script");
            String scriptData = null;
            for (Element script : scripts) {
                if (script.data().contains("initParameter(")) {
                    scriptData = script.data();
                    break;
                }
            }

            if (scriptData == null) {
                return null;
            }

            // initParameter({ ... }); 부분에서 JSON만 추출
            int start = scriptData.indexOf("initParameter(");
            int jsonStart = scriptData.indexOf("{", start);
            int jsonEnd = scriptData.lastIndexOf("})");
            String jsonString = scriptData.substring(jsonStart, jsonEnd + 1);

            // JSON 파싱
            JsonNode root = objectMapper.readTree(jsonString);
            JsonNode initValue = root.path("initValue");

            // 중첩된 JSON 문자열 파싱
            JsonNode dmWlfareInfo = objectMapper.readTree(initValue.get("dmWlfareInfo").asText());
            JsonNode dsWlfareInfoDtl = objectMapper.readTree(initValue.get("dsWlfareInfoDtl").asText());

            // 각 필드 추출
            String targetDetail = cleanText(dmWlfareInfo.path("wlfareSprtTrgtCn").asText());
            String criteria = cleanText(dmWlfareInfo.path("wlfareSprtTrgtSlcrCn").asText());
            String serviceContent = cleanText(dmWlfareInfo.path("wlfareSprtBnftCn").asText());
            String applyMethod = cleanText(dmWlfareInfo.path("aplyMtdDc").asText());
            String tel = extractByDtlCdJoined(dsWlfareInfoDtl, "010");
            String referenceLink = extractByDtlCdJoined(dsWlfareInfoDtl, "020");
            String reference = extractByDtlCdJoined(dsWlfareInfoDtl, "030");

            return CrawlingResponse.builder()
                    .targetDetail(targetDetail)
                    .criteria(criteria)
                    .content(serviceContent)
                    .applyMethod(applyMethod)
                    .tel(tel)
                    .reference(reference)
                    .referenceLink(referenceLink)
                    .build();

        } catch (Exception e) {
            System.out.println("크롤링 실패: " + link);
            return null;
        }
    }

    private String cleanText(String raw) {
        return raw.replaceAll("(?i)<br\\s*/?>", "\n")
                .replaceAll("\\\\n", "\n")
                .replaceAll("&nbsp;", " ")
                .trim();
    }

    private String extractByDtlCdJoined(JsonNode array, String code) {
        StringBuilder builder = new StringBuilder();
        for (JsonNode node : array) {
            if (code.equals(node.path("wlfareInfoDtlCd").asText())) {
                String name = node.path("wlfareInfoReldNm").asText("").trim();
                JsonNode valueNode = node.get("wlfareInfoReldCn");
                String value = (valueNode != null && !valueNode.isNull()) ? valueNode.asText().trim() : "";

                if (!name.isBlank() && !value.isBlank()) {
                    if (builder.length() > 0) builder.append("\n");
                    builder.append(name).append(" ").append(value);
                } else if (!name.isBlank()) {
                    if (builder.length() > 0) builder.append("\n");
                    builder.append(name);
                } else if (!value.isBlank()) {
                    if (builder.length() > 0) builder.append("\n");
                    builder.append(value);
                }
            }
        }
        return builder.toString();
    }

}
