package com.dohee.board.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.dohee.board.dto.Board;
import com.dohee.board.service.BoardService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;
    
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
    public String select(@RequestParam("no")int no, Model model) throws Exception {
        Board board = boardService.select(no);
        log.info("게시글 조회 요청");
        log.info(board.toString());

        model.addAttribute("board", board);

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
        log.info("게시글 등록 요청");

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
    public String update(Model model) {
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

    @GetMapping("/delete")
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
