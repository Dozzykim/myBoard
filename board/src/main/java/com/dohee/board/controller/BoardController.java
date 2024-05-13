package com.dohee.board.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dohee.board.dto.Board;
import com.dohee.board.service.BoardService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Controller     // 컨트롤러 스프링 빈으로 등록 -> 여러가지 요청경로 매핑을 사용할 수 있도록
@RequestMapping("/board")   // 클래스 레벨 요청 경로 매핑 -> /board/~ 경로의 요청은 이 컨트롤러가 처리함
public class BoardController {
    
    @Autowired
    private BoardService boardService;

    /**
     * 전체 게시글 조회
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping("/list")
    public String list(Model model) throws Exception {

        List<Board> boardList = boardService.list();

        // 모델 등록
        model.addAttribute("boardList", boardList);

        // 뷰 페이지 지정
        return "/board/list";    // resources/templates/board/list.html
    }

    /**
     * 게시글 조회 화면
     * /board/read?no=
     * @param model
     * @param no
     * @return
     * @throws Exception 
     */
    @GetMapping("/read")         // @RequestParam("파라미터명") -> 스프링부트 3.2 버전 이하는 생략가능/ 이후는 필수로 명시해야 매핑됨.
    public String read(Model model, @RequestParam("no") int no) throws Exception {

        // 데이터 요청
        Board board = boardService.select(no);

        // 조회수 증가
        int views = boardService.updateViews(board);
        
        // 모델 등록
        model.addAttribute("board", board);

        // 뷰 페이지 지정
        return "/board/read";
    }

    /**
     * 게시글 등록 화면
     * @return
     */
    @GetMapping("/insert")
    public String insert() {
        return "/board/insert";
    }

    /**
     * 게시글 등록 처리
     * @param board
     * @return
     */
    @PostMapping("/insert")
    public String insertPro(Board board) throws Exception {

        log.info(board.toString());

        int result = boardService.insert(board);

        if (result == 0 ) {
            return "/board/insert";
        }
        
        return "redirect:/board/list";
    }
    
    /**
     * 게시글 수정 화면
     * @param model
     * @param no
     * @return
     */
    @GetMapping("/update")
    public String update(Model model, @RequestParam int no) throws Exception {

         // 데이터 요청
         Board board = boardService.select(no);
        
         // 모델 등록
         model.addAttribute("board", board);
 
         // 뷰 페이지 지정
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

        int result = boardService.update(board);
        log.info("컨트롤러 update 함수 진입");
        if (result == 0) {
            int no = board.getNo();
            return "/board/update?no=" + no;
        }

        return "redirect:/board/list";
    }
    
    /**
     * 게시글 삭제 처리
     * @param no
     * @return
     * @throws Exception
     */
    @PostMapping("/delete")
    public String delete(@RequestParam("no") int no) throws Exception {
        
        int result = boardService.delete(no);

        if (result==0) {
            return "/board/update?no=" + no;
        }

        return "redirect:/board/list";
    }
}
