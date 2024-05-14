package com.dohee.board.dto;

import java.util.Date;

import lombok.Data;

@Data
public class Comment {
    
    private int no;         // 댓글 번호
    private int boardNo;    // 종속된 게시글 번호
    private int parentNo;   // 부모댓글 번호 (답댓인 경우 부모 댓글번호/ 본댓인 경우는 자신의 댓글번호)
    private String writer;  // 작성자
    private String content; // 내용
    private Date regDate;   // 작성일자
    private Date updDate;   // 작성일자
}
