package com.myinvo.inkwise.service;

import com.myinvo.inkwise.model.Board;
import com.myinvo.inkwise.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // CREATE
    public Board createBoard(Board board) {
        board.setCreatedAt(LocalDateTime.now());
        board.setUpdatedAt(LocalDateTime.now());
        return boardRepository.save(board);
    }

    // GET ALL
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    // GET BY ID
    public Board getBoardById(String id) {
        return boardRepository.findById(id).orElse(null);
    } 
       public List<Board> getBoardsByUser(String userId) {
        return boardRepository.findByOwnerUserId(userId);
    }

        public List<Board> getBoardsByOwner(String ownerUserId) {
        return boardRepository.findByOwnerUserId(ownerUserId);
    }
}
