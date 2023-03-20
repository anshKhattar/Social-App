package com.social.app.service;

import com.social.app.dto.VoteCountDTO;
import com.social.app.enums.VoteTypeEnum;
import com.social.app.model.VoteModel;
import com.social.app.repository.VoteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepo voteRepo;

    public List<VoteModel> getVotesByPostId(String postId){
        return voteRepo.findAllByPostId(postId);
    }
    public VoteCountDTO getVoteCountByPost(String postId){
        Number likeCount = voteRepo.countAllByPostIdAndVoteType(postId, VoteTypeEnum.LIKE);
        Number dislikeCount = voteRepo.countAllByPostIdAndVoteType(postId, VoteTypeEnum.DISLIKE);
        return VoteCountDTO.builder()
                .likeCount(likeCount)
                .dislikeCount(dislikeCount)
                .build();
    }

    public VoteModel getVoteByPostIdAndUserId(String postId, String userId){
        return voteRepo.findAllByPostIdAndUserId(postId,userId).orElse(null);
    }
    public VoteModel createVote(VoteModel newVote){
        return voteRepo.save(newVote);
    }

    public void deleteVote(String voteId){
        voteRepo.deleteById(voteId);
        return;
    }
    public VoteModel getVoteById(String voteId){
        return voteRepo.findById(voteId).get();
    }
    public VoteModel updateVote(String voteId, VoteTypeEnum voteType){
        VoteModel dbVote = getVoteById(voteId);
        dbVote.setVoteType(voteType);
        return voteRepo.save(dbVote);
    }
    public void deleteAllByPostId(String postId){
        voteRepo.deleteAllByPostId(postId);
    }

}
