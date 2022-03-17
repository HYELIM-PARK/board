package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.repository.BoardRepository;
import com.study.board.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class BoardController {

    @Autowired
    private BoardService boardService;


    @GetMapping("/board/write") //localhost:8080/board/write
    public String boardWriteForm() {

        return "boardwrite";
    }
    
    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model, MultipartFile file) throws Exception {

        boardService.boardWrite(board, file);

        model.addAttribute("message", "글 작성이 완료되었습니다");
        model.addAttribute("searchUrl", "/board/list");

        return "message";
    }

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
                            String searchKeyword) {

        Page<Board> list = null;

        // 검색
        if (searchKeyword == null) {
            list = boardService.boardList(pageable);
        } else {
            list = boardService.boardSearchList(searchKeyword, pageable);
        }

        // 페이징
        //int number = list.getPageable().getPageNumber();
        //int nowPage = list.getPageable().getPageSize() + 1;
        //int totalPages = list.getTotalPages();
        //int start = (number/10)*10 + 1;
        //int last  = start + 9 < totalPages ? start + 9 : totalPages;



        //int nowPage = list.getPageable().getPageNumber() + 1; // 현재 페이지 (Pageable은 0부터 시작하기 때문에 1 더해줌)
        //int startPage = Math.max(nowPage - 5, 1);
        //int endPage = Math.min(nowPage + 5, list.getTotalPages());

        model.addAttribute("list", list);
        //model.addAttribute("number", number);
        //model.addAttribute("nowPage", nowPage);
        //model.addAttribute("totalPages", totalPages);
        //model.addAttribute("start", start);
        //model.addAttribute("last", last);





        //model.addAttribute("nowPage", nowPage);
        //model.addAttribute("startPage", startPage);
        //model.addAttribute("endPage", endPage);

        return "boardlist";
    }

    @GetMapping("/board/view")
    public String boardView(Model model, Integer id) {

        model.addAttribute("board", boardService.boardView(id));

        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id, Model model) {

        boardService.boardDelete(id);

        model.addAttribute("message", "글 삭제가 완료되었습니다");
        model.addAttribute("searchUrl", "/board/list");

        //return "redirect:/board/list";
        return "message";
    }

    @GetMapping("/board/modify/{id}")
    public String boardModify(@PathVariable("id") Integer id, Model model) {

        model.addAttribute("board", boardService.boardView(id));

        return "boardmodify";
    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Board board, Model model, MultipartFile file) throws Exception {

        Board boardTemp = boardService.boardView(id);
        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.boardWrite(boardTemp, file);

        model.addAttribute("message", "수정이 완료되었습니다");
        model.addAttribute("searchUrl", "/board/list");

        //return "redirect:/board/list";
        return "message";
    }

}
