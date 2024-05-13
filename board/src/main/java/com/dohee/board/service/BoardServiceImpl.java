package com.dohee.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dohee.board.dto.Board;
import com.dohee.board.mapper.BoardMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardServiceImpl implements BoardService{
    
    @Autowired
    private BoardMapper boardMapper;
    
    // 게시글 목록
    @Override
    public List<Board> list() throws Exception {

        List<Board> boardList = boardMapper.list();

        return boardList;
    }

    // 게시글 조회
    @Override
    public Board select(int no) throws Exception {

        Board board = boardMapper.select(no);

        return board;
    }

    // 게시글 등록
    @Override
    public int insert(Board board) throws Exception {
        
        // 게시글 등록 처리
        int result = boardMapper.insert(board);

        if (result == 0) {
            return result;
        }
        
        // 첨부파일 처리
        List<MultipartFile> fileList = board.getFileList();

        if (fileList.isEmpty()) {
            return result; // result == 1 (게시글 등록 성공)
        }

        for (MultipartFile file : fileList) {
            if (file.isEmpty()) {
                continue;
            }
            
            
        }


        return result;
    }

    // 게시글 수정
    @Override
    public int update(Board board) throws Exception {

        int result = boardMapper.update(board);

        return result;
    }

    // 게시글 삭제
    @Override
    public int delete(int no) throws Exception {

        int result = boardMapper.delete(no);

        return result;
    }


    // 조회수 증가
    @Override
    public int updateViews(Board board) throws Exception {

        int result = boardMapper.updateViews(board);

        return result;
    }
    

}
