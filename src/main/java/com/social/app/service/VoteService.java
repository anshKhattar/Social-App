package com.social.app.service;

import com.social.app.enums.VoteTypeEnum;
import com.social.app.model.VoteModel;
import com.social.app.repository.VoteRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepo voteRepo;

//    public List<VoteModel> getVotesByPostId(String postId){
//        return voteRepo.findByPost_id(postId);
//    }
//    public VoteModel getVoteByUserAndPost(String userId, String postId){
//        return voteRepo.findByPost_idAndAndUser_id(postId,userId).get();
//    }
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

}
