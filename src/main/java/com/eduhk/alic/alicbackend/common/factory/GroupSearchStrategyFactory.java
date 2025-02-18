package com.eduhk.alic.alicbackend.common.factory;

import com.eduhk.alic.alicbackend.common.constant.GroupDemonTypeEnum;
import com.eduhk.alic.alicbackend.service.AllRoomGroupSearchStrategy;
import com.eduhk.alic.alicbackend.service.GroupSearchStrategy;
import com.eduhk.alic.alicbackend.service.JoinedRoomGroupSearchStrategy;
import com.eduhk.alic.alicbackend.service.PublicRoomGroupSearchStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author FuSu
 * @date 2025/2/15 15:01
 */
@Component
public class GroupSearchStrategyFactory {

    private final Map<GroupDemonTypeEnum, GroupSearchStrategy> strategyMap;

    @Autowired
    public GroupSearchStrategyFactory(AllRoomGroupSearchStrategy allRoomStrategy,
                                      JoinedRoomGroupSearchStrategy joinedRoomStrategy,
                                      PublicRoomGroupSearchStrategy publicRoomStrategy) {
        strategyMap = new HashMap<>();
        strategyMap.put(GroupDemonTypeEnum.ALLROOM, allRoomStrategy);
        strategyMap.put(GroupDemonTypeEnum.JOINEDROOM, joinedRoomStrategy);
        strategyMap.put(GroupDemonTypeEnum.PUBLICROOM, publicRoomStrategy);
    }

    public GroupSearchStrategy getStrategy(GroupDemonTypeEnum type) {
        return strategyMap.get(type);
    }
}
