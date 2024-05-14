package com.dohee.board.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.dohee.board.dto.Files;

@Mapper
public interface FileMapper {

    // 파일 조회
    public Files select(int no) throws Exception;
    
    // 파일 목록 - 부모 테이블,게시글번호 기준
    public List<Files> listByParent(Files file) throws Exception;
    
    // 파일 등록
    public int insert(Files file) throws Exception;

    // 파일 삭제
    public int delete(int no) throws Exception;

}
