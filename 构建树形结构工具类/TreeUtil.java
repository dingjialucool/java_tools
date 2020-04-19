package com.chinobot.aiuas.bot_collect.info.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class TreeUtil {

	protected TreeUtil() {

    }

    /**
     * 用于构建树
     *
     * @param nodes nodes
     * @param <T>   <T>
     * @return <T> Tree<T>
     */
    public static <T> List<Tree<T>> build(List<Tree<T>> nodes) {
        if (nodes == null) {
            return null;
        }
        List<Tree<T>> topNodes = new ArrayList<>();
        nodes.forEach(node -> {
            String pid = node.getParentUUid();
            if (StringUtils.isEmpty(pid)) {
                topNodes.add(node);
                return;
            }
            for (Tree<T> n : nodes) {
                String id = n.getId();
                if (id != null && id.equals(pid)) {
                    if (n.getChildren() == null)
                        n.initChildren();
                    n.getChildren().add(node);
                    node.setHasParent(true);
                    n.setHasChildren(true);
                    n.setHasParent(true);
                    return;
                }
            }
            if (topNodes.isEmpty())
                topNodes.add(node);
        });


//        Tree<T> root = new Tree<>();
//        root.setId("0");
//        root.setParentUUid("");
//        root.setTitle("采查 对象");
//        root.setHasParent(false);
//        root.setHasChildren(true);
//        root.setChildren(topNodes);
        return topNodes;
    }
}
