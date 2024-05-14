package com.dohee.board.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.dohee.board.dto.Files;
import com.dohee.board.mapper.FileMapper;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileServiceImpl implements FileService{
    
    @Autowired
    private FileMapper fileMapper;
    
    @Value("${upload.path}")
    private String uploadPath;
    
    // 파일 조회 - 게시글 조회 시 필요
    @Override
    public Files select(int no) throws Exception {
        
        Files file = fileMapper.select(no);

        return file;
    }


    // 파일 목록 - 부모 테이블,게시글번호 기준
    public List<Files> listByParent(Files file) throws Exception {
        log.info("요청 : " + file.getParentNo() + "번");
        List<Files> fileList = fileMapper.listByParent(file);
        log.info(fileList.toString());
        
        return fileList;
    }

    // 파일 등록 - DB
    @Override
    public int insert(Files file) throws Exception {
        int result = fileMapper.insert(file);

        return result;
    }

    // 파일 업로드 - 등록과정에서 실제 경로에 업로드.
    // - 파일 시스템의 해당 파일을 복사 => 파일정보를 DB에 등록 (insert)
    @Override
    public boolean upload(Files file) throws Exception {
        log.info("업로드 요청 file: " + file.getNo());

        MultipartFile requestFile = file.getFile();
        // 파일 정보: 원본 파일명, 용량, 데이터
        String originName = requestFile.getOriginalFilename();
        long fileSize = requestFile.getSize();
        byte[] fileData = requestFile.getBytes();
        
        // 업로드 경로 => application.properties (upload.path)
        // 파일명: UID_파일명.확장자    => 파일명 중복 방지.
        String UUIDName = UUID.randomUUID().toString() + "_" + originName;

        // Java.io의 File 객체 생성 => new File(업로드 경로, 설정할 파일 명)
        File uploadFile = new File(uploadPath, UUIDName);
        // 파일 업로드 (유저가 서버에 요청한 파일을 복사해서 경로에 넣음)
        FileCopyUtils.copy(fileData, uploadFile);
        file.setFileName(UUIDName);
        file.setOriginName(originName);
        
        String filePath = uploadPath + "/"  + UUIDName;
        file.setFilePath(filePath);
        file.setFileSize(fileSize);

        int result = fileMapper.insert(file);

        if (result == 0) {
            return false;
        }
        
        return true;
    }

    @Override
    public Files download(int no) throws Exception {

        Files file = fileMapper.select(no);

        return file;
    }


    @Override
    public int delete(int no) throws Exception {
        int result = fileMapper.delete(no);

        return result;
    }



}
