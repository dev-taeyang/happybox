package com.app.happybox.service.board;

import com.app.happybox.domain.user.MemberDTO;
import com.app.happybox.domain.user.WelfareDTO;
import com.app.happybox.entity.board.ReviewBoard;
import com.app.happybox.entity.board.ReviewBoardDTO;
import com.app.happybox.entity.file.BoardFile;
import com.app.happybox.entity.file.BoardFileDTO;
import com.app.happybox.entity.reply.Reply;
import com.app.happybox.entity.reply.ReplyDTO;
import com.app.happybox.entity.subscript.Subscription;
import com.app.happybox.entity.subscript.SubscriptionDTO;
import com.app.happybox.entity.user.Member;
import com.app.happybox.entity.user.Welfare;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface ReviewBoardService {
    // 상세보기
    public ReviewBoardDTO getDetail(Long id);

    //    작성하기
    public void write(ReviewBoardDTO reviewBoardDTO, Long memberId);

    //    현재 시퀀스 가져오기
    public ReviewBoard getCurrentSequence();

    //    목록 페이징(최신순)
    public Slice<ReviewBoardDTO> getReviewBoards(Pageable pageable);

    //    목록 페이징(인기순)
    public Slice<ReviewBoardDTO> getPopularReviewBoards(Pageable pageable);

    //    마이페이지 나의후기 목록
    public Page<ReviewBoardDTO> findAllByMemberIdDescWithPaging_QueryDSL(Pageable pageable, Long memberId);


    default ReviewBoardDTO reviewBoardToDTO(ReviewBoard reviewBoard){
        return ReviewBoardDTO.builder()
                .id(reviewBoard.getId())
                .boardTitle(reviewBoard.getBoardTitle())
                .boardContent(reviewBoard.getBoardContent())
                .memberDTO(toMemberDTO(reviewBoard.getMember()))
                .welfareDTO(toWelfareDTO(reviewBoard.getWelfare()))
                .boardRegisterDate(LocalDate.now())
                .boardFiles(boardFileToDTO(reviewBoard.getBoardFiles()))
                .build();
    }

    default MemberDTO toMemberDTO(Member member) {
        return MemberDTO.builder()
                .id(member.getId())
                .userId(member.getUserId())
                .userPassword(member.getUserPassword())
                .userPhoneNumber(member.getUserPhoneNumber())
                .address(member.getAddress())
                .userEmail(member.getUserEmail())
                .userStatus(member.getUserStatus())
                .userRole(member.getUserRole())
                .memberName(member.getMemberName())
                .memberBirth(member.getMemberBirth())
                .memberDeliveryAddress(member.getMemberDeliveryAddress())
                .memberGender(member.getMemberGender())
                .build();
    }

    default WelfareDTO toWelfareDTO(Welfare welfare) {
        return WelfareDTO.builder()
                .id(welfare.getId())
                .userId(welfare.getUserId())
                .userPassword(welfare.getUserPassword())
                .userPhoneNumber(welfare.getUserPhoneNumber())
                .userEmail(welfare.getUserEmail())
                .address(welfare.getAddress())
                .userStatus(welfare.getUserStatus())
                .userRole(welfare.getUserRole())
                .welfareName(welfare.getWelfareName())
                .welfarePointTotal(welfare.getWelfarePointTotal())
                .build();
    }

    default ReviewBoard toReviewBoardEntity(ReviewBoardDTO reviewBoardDTO){
        return ReviewBoard.builder()
                .id(reviewBoardDTO.getId())
                .boardTitle(reviewBoardDTO.getBoardTitle())
                .boardContent(reviewBoardDTO.getBoardContent())
                .boardFiles(reviewBoardDTO.getBoardFiles().stream().map(file -> toBoardFileEntity(file)).collect(Collectors.toList()))
                .member(toMemberEntity(reviewBoardDTO.getMemberDTO()))
                .build();
    }

    default Member toMemberEntity(MemberDTO memberDTO) {
        return Member.builder()
                .userId(memberDTO.getUserId())
                .userPassword(memberDTO.getUserPassword())
                .userPhoneNumber(memberDTO.getUserPhoneNumber())
                .userRole(memberDTO.getUserRole())
                .userRole(memberDTO.getUserRole())
                .memberName(memberDTO.getMemberName())
                .memberBirth(memberDTO.getMemberBirth())
                .memberDeliveryAddress(memberDTO.getMemberDeliveryAddress())
                .userEmail(memberDTO.getUserEmail())
                .memberGender(memberDTO.getMemberGender())
                .build();
    }

    default List<BoardFileDTO> boardFileToDTO(List<BoardFile> boardFiles){
        List<BoardFileDTO> boardFileDTOS = new ArrayList<>();
        boardFiles.forEach(
                boardFile -> {
                    BoardFileDTO boardFileDTO = BoardFileDTO.builder()
                            .id(boardFile.getId())
                            .fileUuid(boardFile.getFileUuid())
                            .filePath(boardFile.getFilePath())
                            .fileOrgName(boardFile.getFileOrgName())
                            .fileRepresent(boardFile.getFileRepresent())
                            .build();
                    boardFileDTOS.add(boardFileDTO);
                }
        );
        return boardFileDTOS;
    }

    default BoardFile toBoardFileEntity(BoardFileDTO boardFileDTO){
        return BoardFile.builder()
                .id(boardFileDTO.getId())
                .fileUuid(boardFileDTO.getFileUuid())
                .filePath(boardFileDTO.getFilePath())
                .fileOrgName(boardFileDTO.getFileOrgName())
                .fileRepresent(boardFileDTO.getFileRepresent())
                .build();
    }

//    default ReviewBoardDTO myReviewBoardToDTO(ReviewBoard reviewBoard){ // 지영이
//        return ReviewBoardDTO.builder()
//                .id(reviewBoard.getId())
//                .memberName(reviewBoard.getMember().getMemberName())
//                .memberId(reviewBoard.getMember().getId())
//                .reviewBoardTitle(reviewBoard.getBoardTitle())
//                .reviewBoardContent(reviewBoard.getBoardContent())
//                .reviewRating(reviewBoard.getReviewRating())
//                .reviewBoardRegisterDate(reviewBoard.getUpdatedDate().toLocalDate())
//                .boardFiles(reviewBoard.getBoardFiles().stream().map(file -> boardFileToDTO(file)).collect(Collectors.toList()))
//                .build();
//    }

}
