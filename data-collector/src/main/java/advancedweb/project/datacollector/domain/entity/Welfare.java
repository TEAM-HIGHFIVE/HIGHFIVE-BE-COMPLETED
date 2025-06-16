package advancedweb.project.datacollector.domain.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Builder
@Getter
public class Welfare {

    @Id
    private String welfareNo;   // PK

    private Summary summary;
    private Detail detail;

    private Integer readCnt;    // 조회 수

    // File
    private String fileNo;      // 복지 서비스 파일 PK
}
