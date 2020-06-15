package com.hankcs.hanlp.summary;

import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * User:krisjin
 * Date:2020-06-11
 */
public class BM25Util {

    final static String default_sentence_separator = "[，,。:：“”？?！!；;]";

    public static void main(String[] args) {
        String s = "图片来源现场：成都青白江区供图5月20日，在“2020年成都市青白江区全球战略合作现场伙伴签约暨揭牌仪式”上，聚焦推动产业功能区高质量发展，现场以成都青白江为核心的“欧洲产业城全球战略合作伙伴产业联盟”正式成立，并举行了现场揭牌仪式。与此同时，成都市青白现场江区人民政府与戴德梁行、中国西南美国商会、现场，普华永道、世邦魏理仕、高力国际、四川省国际合作投资促进会、全球经济发展论坛促进会、德国巴伐利亚州中国";

        BM25 bm25 = new BM25(convertSentenceListToDocument(s));


        List<String> termList = new ArrayList<String>();

        termList.add("现场");
//        termList.add("成都");

        double scores = bm25.sim(termList,3);


        System.err.print(scores);
    }


    /**
     * 将句子列表转化为文档
     *
     * @param document
     * @return
     */
    private static List<List<String>> convertSentenceListToDocument(String document) {
        List<String> sentenceList = splitSentence(document, default_sentence_separator);

        List<List<String>> docs = new ArrayList<List<String>>(sentenceList.size());
        for (String sentence : sentenceList) {
            List<Term> termList = StandardTokenizer.segment(sentence.toCharArray());
            List<String> wordList = new LinkedList<String>();
            for (Term term : termList) {
                if (CoreStopWordDictionary.shouldInclude(term)) {
                    wordList.add(term.word);
                }
            }
            docs.add(wordList);
        }
        return docs;
    }


    /**
     * 将文章分割为句子
     *
     * @param document           待分割的文档
     * @param sentence_separator 句子分隔符，正则表达式，如：   [。:？?！!；;]
     * @return
     */
    private static List<String> splitSentence(String document, String sentence_separator) {
        List<String> sentences = new ArrayList<String>();
        for (String line : document.split("[\r\n]")) {
            line = line.trim();
            if (line.length() == 0) continue;
            for (String sent : line.split(sentence_separator))        // [，,。:：“”？?！!；;]
            {
                sent = sent.trim();
                if (sent.length() == 0) continue;
                sentences.add(sent);
            }
        }

        return sentences;
    }

}
