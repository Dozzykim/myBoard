package com.dohee.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dohee.board.dto.Board;
import com.dohee.board.dto.Files;
import com.dohee.board.mapper.BoardMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BoardServiceImpl implements BoardService{
    
    @Autowired
    private BoardMapper boardMapper;
    
    @Autowired
    private FileService fileService;

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
        
        log.info("게시글 등록 처리");
        // 게시글 등록 처리
        int result = boardMapper.insert(board);
        
        if (result == 0) {
            return result;
        }
        
        // 첨부파일 처리
        String parentTable = "board";
        int parentNo = boardMapper.maxPk();

        List<MultipartFile> fileList = board.getFile();

        if (!fileList.isEmpty()) {
            for (MultipartFile file : fileList) {
                if (file.isEmpty()) {
                    continue;
                }
                // fileList가 비어있지않고, file의 용량이 0이 아닌 사용가능한 파일인 경우.
                // fileService에 매개변수로 넘길 files 객체 세팅
                Files uploadFile = new Files();
                uploadFile.setParentTable(parentTable); // 게시판 정보 
                uploadFile.setParentNo(parentNo);                // 종속된 게시글 번호
                uploadFile.setFile(file);                       // 반복문으로 가져오는 파일 넣기(객체x, 파일자체)
    
                // 파일 업로드 요청
                boolean isUploaded = fileService.upload(uploadFile);

                if (!isUploaded) {
                    log.info("게시글 첨부파일 처리 실패");
                }
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
