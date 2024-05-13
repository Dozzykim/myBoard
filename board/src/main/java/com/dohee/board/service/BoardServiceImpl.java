package com.dohee.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dohee.board.dto.Board;
import com.dohee.board.mapper.BoardMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service    // 서비스 역할의 스프링 빈 등록
public class BoardServiceImpl implements BoardService {
    
    @Autowired
    private BoardMapper boardMapper;

    /**
     * 게시글 전체 조회
     */
    @Override
    public List<Board> list() throws Exception {

        List<Board> boardList = boardMapper.list();

        return boardList;
    }

    /**
     * 게시글 조회
     * - no 매개변수로 게시글 번호를 전달받아서
     *      데이터베이스에 조회 요청
     */
    @Override
    public Board select(int no) throws Exception {

        Board board = boardMapper.select(no);

        return board;
    }

    /**
     * 게시글 등록
     * Board 객체를 매개변수로 전달받아서
     * 데이터베이스에 등록 요청
     */
    @Override
    public int insert(Board board) throws Exception {

        // 게시글 등록 처리
        int result = boardMapper.insert(board);

        return result;
    }

    /**
     * 게시글 수정
     * Board 객체를 매개변수로 전달받아서
     * 데이터베이스에 수정요청
     */
    @Override
    public int update(Board board) throws Exception {
        int result = boardMapper.update(board);

        return result;
    }

    /**
     * 게시글 삭제
     * no 매개변수로 게시글 번호를 전달받아
     * 데이터베이스에 삭제요청
     */
    @Override
    public int delete(int no) throws Exception {
        int result = boardMapper.delete(no);

        return result;
    }

    @Override
    public int updateViews(Board board) throws Exception {
        return boardMapper.updateViews(board);
    }


}

