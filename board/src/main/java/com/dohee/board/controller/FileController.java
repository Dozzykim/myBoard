package com.dohee.board.controller;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dohee.board.dto.Files;
import com.dohee.board.service.FileService;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;

    @Value("${upload.path}")
    private String uploadPath;

    /**
     * 파일 다운로드
     * @param no
     * @param response
     * @throws Exception
     */
    @GetMapping("/{no}")
    public void download(@PathVariable("no")int no, HttpServletResponse response) throws Exception {
        log.info("요청 첨부파일 번호: " + no);
        // DB에서 파일 경로를 읽어오기 위해 Files 객체로 찾아오기
        Files downloadFile = fileService.download(no);

        // ❌다운로드할 파일이 없는 경우
        if (downloadFile == null) {
            // 그냥 메시지만 출력
            return;
        }

        // ⭕다운로드할 파일이 있는 경우
        // DB에서 찾은 파일 객체를 가져와서 파일명과 경로를 꺼내기. (FileInputStream 에서 사용)
        String fileName = downloadFile.getFileName();
        String filePath = downloadFile.getFilePath();

        // 다운로드를 위한 응답헤더 세팅
        // - ContentType        : application/octet-stream
        // - Content-Disposition: attachment, filename="파일명.확장자"
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);

        // 한글 깨짐방지
        fileName = URLEncoder.encode(filePath, "UTF-8");

        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

        // 파일 다운로드 (자바에서 지원하는 File 객체사용)
        // 경로를 생성자에 넣어서 그 경로에 있는 파일을 찾아 넣음.
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);        // 파일 입력
        ServletOutputStream sos = response.getOutputStream();   // 파일 출력
        FileCopyUtils.copy(fis, sos);

        fis.close();
        sos.close();
    }


    /**
     * 첨부파일 출력 (썸네일)
     */
    @GetMapping("/img/{no}")
    public ResponseEntity<byte[]> thumnail(@PathVariable("no")int no) throws Exception {

        // 파일 번호 기준으로 파일정보 조회
        Files file = fileService.select(no);    // 매개변수로 넘겨받은 no에 해당하는 파일 가져옴.

        // Null 체크 
        if (file == null) {
            /* ❌ 첨부파일이 없는 경우 */
            String filePath = uploadPath+"/no-img.jpg";
            File noImgFile = new File(filePath);    // 자바 File 객체에 경로를 넣어서 해당경로의 파일 가져옴
            byte[] noImgFileData = FileCopyUtils.copyToByteArray(noImgFile);    // 파일 데이터를 복사
            
            // 이미지 출력 시, 헤더 세팅
            HttpHeaders headers = new HttpHeaders();        
            headers.setContentType(MediaType.IMAGE_JPEG);

            // ResponseEntity<>(데이터, 헤더, 상태코드)
            return new ResponseEntity<>(noImgFileData, headers, HttpStatus.OK);
        }

        /* ⭕ 첨부파일이 있는 경우 */
        // 조회해서 가져온 file에서 파일 경로꺼내기
        String filePath = file.getFilePath();

        // 자바 File 객체 생성 -> 생성자에 파일 경로 넣어주면 해당 파일이 대입됨.
        File realFile = new File(filePath);
        // fileDAta -> 바이트 형식의 데이터를 가져와줌
        byte[] fileData = FileCopyUtils.copyToByteArray(realFile);

        // 이미지 출력 헤더 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        // ResponseEntity<>(데이터, 헤더, 상태코드)
        return new ResponseEntity<>(fileData, headers, HttpStatus.OK);
    }

    /**
     * 첨부파일 삭제
     * @param no
     * @return
     * @throws Exception
     */
    @DeleteMapping("/{no}")
    public ResponseEntity<String> deleteFile(@PathVariable("no")int no) throws Exception {
        log.info("[DELETE] - /file/" + no);

        int result = fileService.delete(no);

        if (result == 0) {
            // 처리 실패
            log.info("첨부파일 삭제 실패");
            return new ResponseEntity<>("FAIL", HttpStatus.OK);
        }
        
        // 처리 성공
        log.info("첨부파일 삭제 성공");
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }
}
