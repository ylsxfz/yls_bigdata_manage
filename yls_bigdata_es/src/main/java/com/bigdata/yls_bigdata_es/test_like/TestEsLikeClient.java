package com.bigdata.yls_bigdata_es.test_like;

import com.bigdata.yls_bigdata_es.high_rest.config.EsHighLevelConfig;
import com.bigdata.yls_bigdata_es.high_rest.model.EsModel;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Auther: yls
 * @Date: 2020/5/8 11:55
 * @Description:
 * @Version 1.0
 */
public class TestEsLikeClient {

    private static RestHighLevelClient restHighLevelClient = null;

    public static void main(String[] args) throws Exception {
        for (int i = 100000; i < 500000 ; i++) {
            insetData(i+"");
        }
    }

    private static void insetData(String docId) throws IOException, InterruptedException {
        restHighLevelClient = EsHighLevelConfig.init();
        EsModel esModel = new EsModel("yls_like_char_search", "doc", docId);
        IndexRequest indexRequest = new IndexRequest(
                esModel.getIndex(),
                esModel.getType(),
                esModel.getDocId()
        );

        //利用map插入数据
        Map<String, Object> jsonMap = new HashMap<>();
        String name = getName();
        jsonMap.put("analy_name", name);
        jsonMap.put("source_name",name);
        indexRequest.source(jsonMap);

        IndexResponse index = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(index);
//        Thread.sleep(0);
        restHighLevelClient.close();
    }


    public static String getName(){
        String str = "赵钱孙李周吴郑王" +
                "冯陈褚卫蒋沈韩杨" +
                "朱秦尤许何吕施张" +
                "孔曹严华金魏陶姜" +
                "戚谢邹喻柏水窦章" +
                "云苏潘葛奚范彭郎" +
                "鲁韦昌马苗凤花方" +
                "俞任袁柳酆鲍史唐" +
                "费廉岑薛雷贺倪汤" +
                "滕殷罗毕郝邬安常" +
                "乐于时傅皮卞齐康" +
                "伍余元卜顾孟平黄" +
                "和穆萧尹姚邵湛汪" +
                "祁毛禹狄米贝明臧" +
                "计伏成戴谈宋茅庞" +
                "熊纪舒屈项祝董梁" +
                "杜阮蓝闵席季麻强" +
                "贾路娄危江童颜郭" +
                "梅盛林刁钟徐邱骆" +
                "高夏蔡田樊胡凌霍" +
                "虞万支柯昝管卢莫" +
                "经房裘缪干解应宗" +
                "丁宣贲邓郁单杭洪" +
                "包诸左石崔吉钮龚" +
                "程嵇邢滑裴陆荣翁" +
                "荀羊於惠甄曲家封" +
                "芮羿储靳汲邴糜松" +
                "井段富巫乌焦巴弓" +
                "牧隗山谷车侯宓蓬" +
                "全郗班仰秋仲伊宫" +
                "宁仇栾暴甘钭厉戎" +
                "祖武符刘景詹束龙" +
                "叶幸司韶郜黎蓟薄" +
                "印宿白怀蒲邰从鄂" +
                "索咸籍赖卓蔺屠蒙" +
                "池乔阴鬱胥能苍双" +
                "闻莘党翟谭贡劳逄" +
                "姬申扶堵冉宰郦雍" +
                "卻璩桑桂濮牛寿通" +
                "边扈燕冀郏浦尚农" +
                "温别庄晏柴瞿阎充" +
                "慕连茹习宦艾鱼容" +
                "向古易慎戈廖庾终" +
                "暨居衡步都耿满弘" +
                "匡国文寇广禄阙东" +
                "欧殳沃利蔚越夔隆" +
                "师巩厍聂晁勾敖融" +
                "冷訾辛阚那简饶空" +
                "曾毋沙乜养鞠须丰" +
                "巢关蒯相查后荆红" +
                "游竺权逯盖益桓公" +
                "万俟司马上官欧阳" +
                "夏侯诸葛闻人东方" +
                "赫连皇甫尉迟公羊" +
                "澹台公冶宗政濮阳" +
                "淳于单于太叔申屠" +
                "公孙仲孙轩辕令狐" +
                "钟离宇文长孙慕容" +
                "鲜于闾丘司徒司空" +
                "丌官司寇仉督子车" +
                "颛孙端木巫马公西" +
                "漆雕乐正壤驷公良" +
                "拓跋夹谷宰父谷梁" +
                "晋楚闫法汝鄢涂钦" +
                "段干百里东郭南门" +
                "呼延归海羊舌微生" +
                "岳帅缑亢况郈有琴" +
                "梁丘左丘东门西门" +
                "商牟佘佴伯赏南宫" +
                "墨哈谯笪年爱阳佟" +
                "第五言福百家姓终";

        char[] strArr = str.toCharArray();

        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0;i<8;i++){
            Random random = new Random();
            int nextInt = random.nextInt(strArr.length - 1);
            stringBuilder.append(strArr[nextInt]);
        }

        return stringBuilder.toString();
    }
}
