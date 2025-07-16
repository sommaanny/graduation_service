package graduation_service.graduation.pdfTest;

import graduation_service.graduation.domain.pojo.Transcript;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;

@Slf4j
public class PdfTest {

    @Test
    void pdf추출() {
        try {
            Transcript transcript = new Transcript(); //매칭할 성적표 객체 생성

            // 테스트용 PDF 파일 로드
            File file = new File("src/test/resources/sample-transcript.pdf"); // 테스트 PDF 경로
            FileInputStream fis = new FileInputStream(file); //파일을 byte 단위로 읽을 수 있게

            PDDocument document = PDDocument.load(fis); //pdf파일 읽기
            PDFTextStripper stripper = new PDFTextStripper(); //텍스트 추출 도구

            String text = stripper.getText(document); //텍스트 추출

            String[] lines = text.split("\n"); //텍스트를 행 단위로 분리

            for (int i = 0; i < lines.length; i++) {
                String line = lines[i];
                line = line.trim(); //pdf에서 추출한 text 특성상 양 옆에 공백에 있을 가능성이 큼(있어도 상관은 없지만 일관성을 위해)

                if (line.matches("^P\\(.*\\)$")) {
                    for (int j = i + 1; j < i + 24; j++) {
                        String credit = lines[j].trim();
                        if (j == i + 1) {
                            //1. 필수교양
                            int input = Integer.parseInt(credit);
                            transcript.setRequiredGeneralEducationCredits(input);
                        } else if (j == i + 2) {
                            //2. 교양선택
                            int input = Integer.parseInt(credit);
                            transcript.setElectiveGeneralEducationCredits(input);
                        } else if (j == i + 3) {
                            //3. 기초전공
                            int input = Integer.parseInt(credit);
                            transcript.setBasicMajorCredits(input);
                        } else if (j == i + 4) {
                            //4. 전공필수
                            int input = Integer.parseInt(credit);
                            transcript.setRequiredMajorCredits(input);
                        } else if (j == i + 5) {
                            //5. 전공선택
                            int input = Integer.parseInt(credit);
                            transcript.setElectiveMajorCredits(input);
                        } else if (j == i + 17) {
                            //17. 기타학점
                            int input = Integer.parseInt(credit);
                            transcript.setOtherEarnedCredits(input);
                        } else if (j == i + 19) {
                            //19. 총 취득학점
                            int input = Integer.parseInt(credit);
                            transcript.setTotalCredits(input);
                        } else if (j == i + 22) {
                            //22. 종합 성적
                            float input = Float.parseFloat(credit);
                            transcript.setGpa(input);
                        } else if (j == i + 24) {
                            //24. 편입인정학점(전공/전체)
                            String[] result = credit.split("/");
                            int major = Integer.parseInt(result[0]); //전공
                            int total = Integer.parseInt(result[1]); //전체
                            transcript.setTransferredMajorCredits(major);
                            transcript.setTotalTransferredCredits(total);
                        }
                    }
                    break;
                }
                log.info("line : " + line);
            }

            log.info(transcript.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
