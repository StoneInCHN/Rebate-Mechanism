package org.rebate.utils.jiupai.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rebate.utils.LogUtil;

public class RpmVerifyService {
    /**
     * @param signMap        加签字段
     * @param notEmptyFields 非空字段
     * @param emptyFields    可空字段
     */
    public void executeVerify(Map<String, String> signMap, Map<String, String> notEmptyFields, Map<String, String> emptyFields) throws Exception {
        StringBuilder warnMessage = new StringBuilder();
        for (String ef : emptyFields.keySet()) {
            if (!signMap.containsKey(ef)) {
                warnMessage.append(ef).append("=?&");
                continue;
            }
            signMap.remove(ef);
        }
        if (warnMessage.length() > 0){
            String res = warnMessage.substring(0,warnMessage.length() - 1).toString();
            LogUtil.debug(this.getClass(), "verify", "The empty fields are not filled:%s", res);
        }

        List<String> message = new ArrayList<>();
        for (String nef : notEmptyFields.keySet()) {
            if (!signMap.containsKey(nef)) {
                message.add(nef);
                continue;
            }
            signMap.remove(nef);
        }
        Map<Integer, List<String>> messageMap = new HashMap<>();
        if (!message.isEmpty()) {
            messageMap.put(MrpException.MRPCODE, message);
        }
        //多传
        List<String> ext = new ArrayList<>();
        if (!signMap.isEmpty()) {
            for (String extFileds : signMap.keySet()) {
                ext.add(extFileds);
            }
            messageMap.put(MrpException.MPPCODE, ext);
        }
        if (!messageMap.isEmpty()) {
            throw new MrpException(messageMap);
        }
    }
}
