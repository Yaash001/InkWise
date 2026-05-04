package com.myinvo.inkwise.controller;

import com.myinvo.inkwise.model.Board;
import com.myinvo.inkwise.service.BoardService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // CREATE BOARD
    @PostMapping
    public Board createBoard(@RequestBody Board board) {
        return boardService.createBoard(board);
    }

    // GET ALL BOARDS
    @GetMapping
    public List<Board> getAllBoards() {
        return boardService.getAllBoards();
    }

    // GET BOARD BY ID (Mongo ID)
    @GetMapping("/{id}")
    public Board getBoard(@PathVariable String id) {
        return boardService.getBoardById(id);
    }

    // GET BOARDS BY OWNER USER ID
    @GetMapping("/user/{ownerUserId}")
    public List<Board> getBoardsByOwner(@PathVariable String ownerUserId) {
        return boardService.getBoardsByOwner(ownerUserId);
    }
}