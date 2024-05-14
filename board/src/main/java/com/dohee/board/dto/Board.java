package com.dohee.board.dto;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class Board {

    private int no;         // 글번호
    private String title;   // 제목
    private String writer;  // 작성자
    private String content; // 내용
    private Date regDate;   // 작성일자
    private Date updDate;   // 수정일자
    private int views;      // 조회수

    private List<MultipartFile> file; // 첨부파일 목록

}
