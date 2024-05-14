package com.dohee.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dohee.board.dto.Board;
import com.dohee.board.dto.Files;
import com.dohee.board.service.BoardService;
import com.dohee.board.service.FileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private FileService fileService;
    
    /**
     * 게시글 목록
     * @param model
     * @return
     * @throws Exception 
     */
    @GetMapping("/list")
    public String list(Model model) throws Exception {

        List<Board> boardList = boardService.list();
        
        model.addAttribute("boardList", boardList);

        return "/board/list";
    }

    /**
     * 게시글 조회
     * @param no
     * @param model
     * @return
     * @throws Exception 
     */
    @GetMapping("/read")
    public String read(@RequestParam("no")int no, Model model, Files file) throws Exception {
        log.info("요청 게시글: " + no + "번");
        // 데이터 요청
        // - 게시글
        Board board = boardService.select(no);
        // - 첨부파일 목록 ("board"라는 게시판의 게시글번호가 no인 파일들을 가져옴)
        file.setParentTable("board");
        file.setParentNo(no);
        List<Files> fileList = fileService.listByParent(file);

        model.addAttribute("board", board);
        model.addAttribute("fileList", fileList);

        boardService.updateViews(board);

        return "/board/read";
    }

    // 게시글 등록 화면
    @GetMapping("/insert")
    public String insert() {
        return "/board/insert";
    }
    
    /**
     * 게시글 등록 처리
     * @param board
     * @return
     * @throws Exception
     */
    @PostMapping("/insert")
    public String insertPro(Board board) throws Exception {
        log.info("board: " + board);

        // 게시글 등록 처리
        log.info(board.toString());
        int result = boardService.insert(board);

        if (result == 0) {
            log.info("게시글 등록 처리 중 예외 발생");
            return "/board/insert";
        }

        return "redirect:/board/list";
    }

    // 게시글 수정 화면
    @GetMapping("/update")
    public String update(@RequestParam("no")int no, Files file, Model model) throws Exception {
        // 데이터 요청
        // - 게시글
        Board board = boardService.select(no);
        // - 첨부파일 목록 ("board"라는 게시판의 게시글번호가 no인 파일들을 가져옴)
        file.setParentTable("board");
        file.setParentNo(no);
        List<Files> fileList = fileService.listByParent(file);

        model.addAttribute("board", board);
        model.addAttribute("fileList", fileList);

        return "/board/update";
    }

    /**
     * 게시글 수정 처리
     * @param board
     * @return
     * @throws Exception
     */
    @PostMapping("/update")
    public String updatePro(Board board) throws Exception {

        log.info("게시글 수정 요청");
        log.info(board.toString());

        int result = boardService.update(board);

        if (result == 0) {
            log.info("게시글 수정 처리 중 예외 발생");
            return "/board/update";
        }
        return "redirect:/board/list";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("no")int no) throws Exception {
        log.info("삭제요청 글번호: " + no + "번");
        int result = boardService.delete(no);

        if (result == 0) {
            log.info("게시글 삭제 처리 중 예외 발생");
            return "/board/update";
        }

        return "redirect:/board/list";
    }
    
}
