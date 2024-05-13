package com.dohee.board.dto;

import java.util.Date;

import org.springframework.web.multipart.MultipartFile;

public class Files {
    
    private int no;             // 파일번호
    private String parentTable; // 종속 테이블
    private int boardNo;        // 종속 게시글 번호
    private String fileName;    // uid 변형 파일명
    private String originName;  // 기존 파일명
    private String filePath;    // 파일경로
    private Date regDate;       // 등록일자
    private Date updDate;       // 수정일자
    private long fileSize;      // 파일 사이즈

    private int fileCode;       // 썸네일/일반 파일 구분 코드

    private MultipartFile file; // 진짜 파일 담아둘 변수

    /*
     * MultipartFile vs File
     * 
     * ✅MultipartFile
     * : 스프링에서 업로드된 파일을 다룰 때 사용되는 인터페이스로,
     *   파일 이름과 실제 데이터, 파일 크기 등을 구할 수 있음.
     *   (클라이언트가 준 파일을 받아오는 역할)
     * 
     * ✅File
     * : java.io 패키지에 포함되며, 입출력에 필요한 파일이나 디렉터리를 제어하는데 사용.
     *   파일과 디렉터리의 접근 권한, 생성된 시간, 경로 등의 정보를 얻을 수 있는 메서드가 존재.
     *   새로운 파일 및 디렉터리 생성, 삭제 등 다양한 조작 메서드 보유.
     *   (서버측에서 파일 왔다갔다)
     */
}
