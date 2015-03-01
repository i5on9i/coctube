package com.cocube.parser.krcoc;

import com.cocube.parser.ChannelParserInfo;
import com.cocube.parser.PlaylistItemsParserInfo;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 9. 14
 * Time: 오전 12:53
 * To change this template use File | Settings | File Templates.
 */
public class LiarTvCocParserInfo extends ChannelParserInfo {

    private static final String CHANNEL_ID = "UCqgRbodod44OjLp9tqpgkPw";    // https://www.youtube.com/channel/UC4Vxtl8TqjlsPcHzlO7UoIg
    private static final String QUERY = "클래시";    // https://www.youtube.com/channel/UC4Vxtl8TqjlsPcHzlO7UoIg

    public LiarTvCocParserInfo() {
        super(CHANNEL_ID, QUERY);
    }

}
