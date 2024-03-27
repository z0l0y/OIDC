package com.hust.service.Impl;

import com.hust.dto.FriendDTO;
import com.hust.mapper.FriendShipMapper;
import com.hust.po.CollectionPO;
import com.hust.po.FriendShipPO;
import com.hust.po.RatingPO;
import com.hust.service.FriendShipService;
import com.hust.utils.Result;
import com.hust.vo.CollectionVO;
import com.hust.vo.FriendShipVO;
import com.hust.vo.RatingVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.hust.utils.Conversion.*;

@Service
public class FriendShipServiceImpl implements FriendShipService {
    @Autowired
    private FriendShipMapper friendShipMapper;

    @Override
    public Result applyFriend(FriendDTO friendDTO) {
        int rowsAffected = friendShipMapper.applyFriend(friendDTO);
        if (rowsAffected > 0) {
            return Result.success("申请好友成功！");
        } else {
            return Result.error("申请好于失败！");
        }
    }

    @Override
    public Result seePendingMsg(FriendDTO friendDTO) {
        FriendShipPO[] friendShipPOS = friendShipMapper.seePendingMsg(friendDTO);
        List<FriendShipVO> collections = new ArrayList<>();
        for (FriendShipPO friendShipPO : friendShipPOS) {
            FriendShipVO friendShipVO = toFriendShipVO(friendShipPO);
            collections.add(friendShipVO);
        }
        return Result.success(collections);
    }

    @Override
    public Result agreeFriend(FriendDTO friendDTO) {
        friendShipMapper.agreeFriend(friendDTO);
        return Result.success("已同意该用户的好友申请");
    }

    @Override
    public Result disagreeFriend(FriendDTO friendDTO) {
        friendShipMapper.disagreeFriend(friendDTO);
        return Result.success("已拒绝该用户的好友申请！");
    }

    /**
     * 说实话，怎么感觉这个通知好友，和B站上面的推送你关注的人的动态，情景是一样的。嗯，就是一样的
     *
     * @param friendDTO
     * @return
     */
    @Override
    public Result notifyUser1(FriendDTO friendDTO) {
        FriendShipPO friendShipPO1 = friendShipMapper.verifyFriend1(friendDTO);
        FriendShipPO friendShipPO2 = friendShipMapper.verifyFriend2(friendDTO);
        if (friendShipPO1 == null && friendShipPO2 == null) {
            return Result.error("您还不是该用户的好友，成为好友之后才能通知动态哦！");
        }
        RatingPO[] ratingPO = friendShipMapper.notifyUser1(friendDTO);
        List<RatingVO> list = new ArrayList<>();
        for (RatingPO ratingPO1 : ratingPO) {
            RatingVO ratingVO = toRatingVO(ratingPO1);
            list.add(ratingVO);
        }
        return Result.success(list);
    }
}
