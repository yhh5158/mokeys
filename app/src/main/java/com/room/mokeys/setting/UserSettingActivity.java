package com.room.mokeys.setting;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.room.mokeys.R;
import com.room.mokeys.kit.Constants;
import com.room.mokeys.model.MokeysListModel;
import com.room.mokeys.net.Api;

import org.sufficientlysecure.htmltextview.HtmlHttpImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.droidlover.xdroidmvp.kit.SPUtil;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import cn.droidlover.xdroidmvp.net.ApiSubscriber;
import cn.droidlover.xdroidmvp.net.NetError;
import cn.droidlover.xdroidmvp.net.XApi;

/**
 * Created by yhh5158 on 2017/7/18.
 */

public class  UserSettingActivity extends XActivity {
    @BindView(R.id.html_text)
    HtmlTextView htmlTextView;
    @BindView(R.id.webView)
    WebView webView;
    String mHtml = "<p style=\"font-size:16px;color:#333333;line-height:30px;word-break:break-all;font-family:Helvetica,Arial,sans-serif;font-weight:normal;\" data-flag=\"normal\">\r\n\t<span>▲每周设计一个聆听主题，有大师详解、有协奏曲、奏鸣曲等专场，有比较轻松的音乐治疗、大师小品文，也有如何聆听音乐会、如何挑选唱片的指南……周一到周五，满满的情怀和干货。</span> \r\n</p>\r\n<p style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica, Arial, sans-serif;font-weight:normal;text-align:left;\" data-flag=\"normal\" lang=\"en\">\r\n\t▲每周六设一期周末特别福利，对接下来一周的分享进行一个简单的概括和预告。\r\n</p>\r\n<p style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica, Arial, sans-serif;font-weight:normal;text-align:left;\" data-flag=\"normal\" lang=\"en\">\r\n\t<span>▲每周一到周五，推送一首相关的精选曲目 。</span> \r\n</p>\r\n<p style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica, Arial, sans-serif;font-weight:normal;text-align:left;\" data-flag=\"normal\" lang=\"en\">\r\n\t<br />\r\n</p>\r\n<p style=\"font-size:16px;color:#333333;line-height:30px;word-break:break-all;font-family:Helvetica, Arial, sans-serif;font-weight:normal;\" data-flag=\"normal\">\r\n\t<b>这里分享的音乐将从迷人的巴洛克到颠覆的20世纪音乐，带你穿越音乐发展百年史！</b> \r\n</p>\r\n<p style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica, Arial, sans-serif;font-weight:normal;text-align:left;\" data-flag=\"normal\" lang=\"en\">\r\n\t<b><br />\r\n</b> \r\n</p>\r\n<p style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica, Arial, sans-serif;font-weight:normal;text-align:left;\" data-flag=\"normal\" lang=\"en\">\r\n\t<b>1. 穿越巴洛克</b> \r\n</p>\r\n<p style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica, Arial, sans-serif;font-weight:normal;text-align:left;\" data-flag=\"normal\" lang=\"en\">\r\n\t<b><img data-key=\"0\"  src=\"http://fdfs.xmcdn.com/group30/M07/78/54/wKgJWlnNwhySNtr3AADiXY-dp0M503_mobile_large.jpg\" alt=\"\" data-origin=\"http://fdfs.xmcdn.com/group30/M07/78/54/wKgJWlnNwhySNtr3AADiXY-dp0M503.jpg\" data-large=\"http://fdfs.xmcdn.com/group30/M07/78/54/wKgJWlnNwhySNtr3AADiXY-dp0M503_mobile_large.jpg\" data-large-width=\"750\" data-large-height=\"421\" data-preview=\"http://fdfs.xmcdn.com/group30/M07/78/54/wKgJWlnNwhySNtr3AADiXY-dp0M503_mobile_small.jpg\" data-preview-width=\"140\" data-preview-height=\"78\"><br />\r\n</b> \r\n</p>\r\n<p style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica, Arial, sans-serif;font-weight:normal;text-align:left;\" data-flag=\"normal\" lang=\"en\">\r\n\t巴洛克时期音乐有很浓的宗教色彩，器乐曲发展也很迅速，尤其是弦乐方面的发展，弦乐的音色更能体现出巴洛克的特色。节目中我们将会听到这一时期代表音乐家有：巴赫、亨德尔、维瓦尔第等。<br />\r\n<b><br />\r\n2.古典风格</b> \r\n</p>\r\n<p style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica, Arial, sans-serif;font-weight:normal;text-align:left;\" data-flag=\"normal\" lang=\"en\">\r\n\t<b><img data-key=\"1\"  src=\"http://fdfs.xmcdn.com/group30/M07/78/58/wKgJWlnNwjrizRG0AADlTa-dRD0590_mobile_large.jpg\" alt=\"\" data-origin=\"http://fdfs.xmcdn.com/group30/M07/78/58/wKgJWlnNwjrizRG0AADlTa-dRD0590.jpg\" data-large=\"http://fdfs.xmcdn.com/group30/M07/78/58/wKgJWlnNwjrizRG0AADlTa-dRD0590_mobile_large.jpg\" data-large-width=\"750\" data-large-height=\"421\" data-preview=\"http://fdfs.xmcdn.com/group30/M07/78/58/wKgJWlnNwjrizRG0AADlTa-dRD0590_mobile_small.jpg\" data-preview-width=\"140\" data-preview-height=\"78\"><br />\r\n</b> \r\n</p>\r\n<p style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica, Arial, sans-serif;font-weight:normal;text-align:left;\" data-flag=\"normal\" lang=\"en\">\r\n\t古典时期的音乐是理智和情感的高度统一；深刻的思想内容与完美的艺术形式的高度统一。创作技法上，继承欧洲传统的复调与主调音乐的成就，并确立了近代鸣奏曲曲式的结构以及交响曲、协奏曲、各类室内乐的体裁和形式，对西洋音乐的发展有深远影响。节目中我们将会听到这一时期代表音乐家有：海顿、莫扎特、贝多芬。<br />\r\n<b><br />\r\n3. 革命与浪漫</b> \r\n</p>\r\n<p style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica, Arial, sans-serif;font-weight:normal;text-align:left;\" data-flag=\"normal\" lang=\"en\">\r\n\t<b><img data-key=\"2\"  src=\"http://fdfs.xmcdn.com/group30/M0B/78/5C/wKgJWlnNwl6hDus4AADyPB6owFU650_mobile_large.jpg\" alt=\"\" data-origin=\"http://fdfs.xmcdn.com/group30/M0B/78/5C/wKgJWlnNwl6hDus4AADyPB6owFU650.jpg\" data-large=\"http://fdfs.xmcdn.com/group30/M0B/78/5C/wKgJWlnNwl6hDus4AADyPB6owFU650_mobile_large.jpg\" data-large-width=\"750\" data-large-height=\"421\" data-preview=\"http://fdfs.xmcdn.com/group30/M0B/78/5C/wKgJWlnNwl6hDus4AADyPB6owFU650_mobile_small.jpg\" data-preview-width=\"140\" data-preview-height=\"78\"><br />\r\n</b>浪漫主义时期艺术家的创作上则表现为对主观感情的崇尚，对自然的热爱和对未来的幻想。这个突破了古典音乐均衡完整的形式结构的限制，有更大的自由性。节目中我们将会听到这一时期代表音乐家有：肖邦、李斯特、瓦格纳、勃拉姆斯、老柴、拉赫玛尼诺夫等……\r\n</p>\r\n<p style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica, Arial, sans-serif;font-weight:normal;text-align:left;\" data-flag=\"normal\" lang=\"en\">\r\n\t<br />\r\n</p>\r\n<p style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica, Arial, sans-serif;font-weight:normal;text-align:left;\" data-flag=\"normal\" lang=\"en\">\r\n\t<b>4. 颠覆的20世纪</b> \r\n</p>\r\n<p style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica, Arial, sans-serif;font-weight:normal;text-align:left;\" data-flag=\"normal\" lang=\"en\">\r\n\t<b><img data-key=\"3\"  src=\"http://fdfs.xmcdn.com/group30/M09/56/14/wKgJXlnN2O2g8YMBAADrHyJrOjM454_mobile_large.jpg\" alt=\"\" data-origin=\"http://fdfs.xmcdn.com/group30/M09/56/14/wKgJXlnN2O2g8YMBAADrHyJrOjM454.jpg\" data-large=\"http://fdfs.xmcdn.com/group30/M09/56/14/wKgJXlnN2O2g8YMBAADrHyJrOjM454_mobile_large.jpg\" data-large-width=\"750\" data-large-height=\"421\" data-preview=\"http://fdfs.xmcdn.com/group30/M09/56/14/wKgJXlnN2O2g8YMBAADrHyJrOjM454_mobile_small.jpg\" data-preview-width=\"140\" data-preview-height=\"78\"><br />\r\n</b> \r\n</p>\r\n<p style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica, Arial, sans-serif;font-weight:normal;text-align:left;\" data-flag=\"normal\" lang=\"en\">\r\n\t20世纪世界风云变幻，这种历史和政治氛围为音乐的多元发展提供了无限灵感。从埃尔加到布里顿，从斯特拉文斯基到格什温，从约翰·威廉斯到飞利浦·格拉斯，音乐在这个时期既颠覆传统也在不断地探求回归传统。先进的录音技术创新也塑造了像帕瓦罗蒂、卡拉斯这样的巨星。<span>节目中我们将会听到这一时期代表音乐家有：斯特拉文斯基、肖斯塔科维奇、飞利浦·格拉斯等……</span> \r\n</p>";
    String mIntro = "<p style=\"font-size:16px;color:#333333;line-height:30px;word-break:break-all;font-family:Helvetica,Arial,sans-serif;font-weight:normal\" data-flag=\"normal\"><span><b><u>课程介绍</u></b></span></p><span><br /></span><p style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica, Arial, sans-serif;font-weight:normal;text-align:left\" data-flag=\"normal\"><span>《田艺苗：古典音乐很难吗》这个节目，不同于艰深的音乐课堂，也不同于普通的播放音乐的广播节目，而是带领大家轻松又有体系的学会聆听古典音乐。古典音乐很难，但是田艺苗老师会提供一些方法和途径，帮助大家更快入门，一起感受音乐的力量与感动。</span></p><span><br /></span><p style=\"font-size:16px;color:#333333;line-height:30px;word-break:break-all;font-family:Helvetica, Arial, sans-serif;font-weight:normal\" data-flag=\"normal\"><b>每天10分钟，穿T恤欣赏古典音乐。</b>只要您每天花一些零碎的时间，通过有目的性的聆听练习，就会对某种音乐体裁、某一大师的作曲方法有较为深入的认知，对古典音乐有一个全面的了解。</p><span><br /></span><p data-flag=\"normal\" style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica,Arial,sans-serif;font-weight:normal;text-align:left;hyphens:auto\"><b><u>本次课程你将获得</u></b></p><span><br /></span><p data-flag=\"normal\" style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica, Arial, sans-serif;font-weight:normal;text-align:left\"><b>眼界：</b>了解古典音乐背后的故事</p><p data-flag=\"normal\" style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica,Arial,sans-serif;font-weight:normal;text-align:left;hyphens:auto\"><b>知识：</b>系统地学习到古典音乐有关的知识</p><p data-flag=\"normal\" style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica,Arial,sans-serif;font-weight:normal;text-align:left;hyphens:auto\"><b>实用：</b>了解古典音乐是否应该鼓掌，如何着装等古典音乐礼仪</p><p data-flag=\"normal\" style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica,Arial,sans-serif;font-weight:normal;text-align:left;hyphens:auto\"><b>欣赏：</b>学会用更轻松但更专业的方法聆听古典音乐</p><p data-flag=\"normal\" style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica,Arial,sans-serif;font-weight:normal;text-align:left;hyphens:auto\"><b>共鸣：</b>将古典音乐和生活联系起来，在古典音乐里汲取美和精神力量</p><p style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica, Arial, sans-serif;font-weight:normal;text-align:left\" data-flag=\"normal\"><span><img data-key=\"0\" src=\"http://fdfs.xmcdn.com/group30/M0A/76/1D/wKgJWlnNrrjh0j75AAFAzghJubQ70_mobile_large.jpeg\" alt=\"\" data-origin=\"http://fdfs.xmcdn.com/group30/M0A/76/1D/wKgJWlnNrrjh0j75AAFAzghJubQ70.jpeg\" data-large=\"http://fdfs.xmcdn.com/group30/M0A/76/1D/wKgJWlnNrrjh0j75AAFAzghJubQ70_mobile_large.jpeg\" data-large-width=\"750\" data-large-height=\"1011\" data-preview=\"http://fdfs.xmcdn.com/group30/M0A/76/1D/wKgJWlnNrrjh0j75AAFAzghJubQ70_mobile_small.jpeg\" data-preview-width=\"140\" data-preview-height=\"188\" /><br /></span></p><span><br /></span><p style=\"font-size:16px;color:#333333;line-height:30px;word-break:break-all;font-family:Helvetica,Arial,sans-serif;font-weight:normal\" data-flag=\"normal\"><span><b><u>适宜人群</u></b></span></p><p style=\"font-size:16px;color:#333333;line-height:30px;word-break:break-all;font-family:Helvetica,Arial,sans-serif;font-weight:normal\" data-flag=\"normal\"><span><img data-key=\"1\" alt=\"\" src=\"http://image.xmcdn.com/group21/M01/4D/E2/wKgJKFiT8fSxlPKnAAFItVf_pXc85.jpeg?op_type=4&device_type=ios&upload_type=attachment&name=mobile_large\" data-large-width=\"750\" data-large-height=\"862\" data-preview-width=\"140\" data-preview-height=\"160\" data-large=\"http://image.xmcdn.com/group21/M01/4D/E2/wKgJKFiT8fSxlPKnAAFItVf_pXc85.jpeg?op_type=4&device_type=ios&upload_type=attachment&name=mobile_large\" data-preview=\"http://image.xmcdn.com/group21/M01/4D/E2/wKgJKFiT8fSxlPKnAAFItVf_pXc85.jpeg?op_type=4&device_type=ios&upload_type=attachment&name=mobile_small\" data-origin=\"http://fdfs.xmcdn.com/group21/M01/4D/E2/wKgJKFiT8fSxlPKnAAFItVf_pXc85.jpeg\" /></span></p><span><br /></span><p style=\"font-size:16px;color:#333333;line-height:30px;word-break:break-all;font-family:Helvetica, Arial, sans-serif;font-weight:normal\" data-flag=\"normal\"><span><span><b>古典音乐很难吗？其实巴赫、贝多芬、莫扎特、肖邦、柴可夫斯基……就在你的身边，只要你打开那扇尘封已久的窗。</b></span></span></p>";
    @Override
    public void initData(Bundle savedInstanceState) {
//        mTxView.setText(Html.fromHtml(mHtml));
//        mTxView.setText(Html.fromHtml(mIntro));
        htmlTextView.setHtml("<p style=\"font-size:16px;color:#333333;line-height:30px;word-break:break-all;font-family:Helvetica,Arial,sans-serif;font-weight:normal\" data-flag=\"normal\"><span><b><u>课程介绍</u></b></span></p><span><br /></span><p style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica, Arial, sans-serif;font-weight:normal;text-align:left\" data-flag=\"normal\"><span>《田艺苗：古典音乐很难吗》这个节目，不同于艰深的音乐课堂，也不同于普通的播放音乐的广播节目，而是带领大家轻松又有体系的学会聆听古典音乐。古典音乐很难，但是田艺苗老师会提供一些方法和途径，帮助大家更快入门，一起感受音乐的力量与感动。</span></p><span><br /></span><p style=\"font-size:16px;color:#333333;line-height:30px;word-break:break-all;font-family:Helvetica, Arial, sans-serif;font-weight:normal\" data-flag=\"normal\"><b>每天10分钟，穿T恤欣赏古典音乐。</b>只要您每天花一些零碎的时间，通过有目的性的聆听练习，就会对某种音乐体裁、某一大师的作曲方法有较为深入的认知，对古典音乐有一个全面的了解。</p><span><br /></span><p data-flag=\"normal\" style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica,Arial,sans-serif;font-weight:normal;text-align:left;hyphens:auto\"><b><u>本次课程你将获得</u></b></p><span><br /></span><p data-flag=\"normal\" style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica, Arial, sans-serif;font-weight:normal;text-align:left\"><b>眼界：</b>了解古典音乐背后的故事</p><p data-flag=\"normal\" style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica,Arial,sans-serif;font-weight:normal;text-align:left;hyphens:auto\"><b>知识：</b>系统地学习到古典音乐有关的知识</p><p data-flag=\"normal\" style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica,Arial,sans-serif;font-weight:normal;text-align:left;hyphens:auto\"><b>实用：</b>了解古典音乐是否应该鼓掌，如何着装等古典音乐礼仪</p><p data-flag=\"normal\" style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica,Arial,sans-serif;font-weight:normal;text-align:left;hyphens:auto\"><b>欣赏：</b>学会用更轻松但更专业的方法聆听古典音乐</p><p data-flag=\"normal\" style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica,Arial,sans-serif;font-weight:normal;text-align:left;hyphens:auto\"><b>共鸣：</b>将古典音乐和生活联系起来，在古典音乐里汲取美和精神力量</p><p style=\"font-size:16px;color:#333333;line-height:30px;font-family:Helvetica, Arial, sans-serif;font-weight:normal;text-align:left\" data-flag=\"normal\"><span><img data-key=\"0\" src=\"http://fdfs.xmcdn.com/group30/M0A/76/1D/wKgJWlnNrrjh0j75AAFAzghJubQ70_mobile_large.jpeg\" alt=\"\" data-origin=\"http://fdfs.xmcdn.com/group30/M0A/76/1D/wKgJWlnNrrjh0j75AAFAzghJubQ70.jpeg\" data-large=\"http://fdfs.xmcdn.com/group30/M0A/76/1D/wKgJWlnNrrjh0j75AAFAzghJubQ70_mobile_large.jpeg\" data-large-width=\"750\" data-large-height=\"1011\" data-preview=\"http://fdfs.xmcdn.com/group30/M0A/76/1D/wKgJWlnNrrjh0j75AAFAzghJubQ70_mobile_small.jpeg\" data-preview-width=\"140\" data-preview-height=\"188\" /><br /></span></p><span><br /></span><p style=\"font-size:16px;color:#333333;line-height:30px;word-break:break-all;font-family:Helvetica,Arial,sans-serif;font-weight:normal\" data-flag=\"normal\"><span><b><u>适宜人群</u></b></span></p><p style=\"font-size:16px;color:#333333;line-height:30px;word-break:break-all;font-family:Helvetica,Arial,sans-serif;font-weight:normal\" data-flag=\"normal\"><span><img data-key=\"1\" alt=\"\" src=\"http://image.xmcdn.com/group21/M01/4D/E2/wKgJKFiT8fSxlPKnAAFItVf_pXc85.jpeg?op_type=4&device_type=ios&upload_type=attachment&name=mobile_large\" data-large-width=\"750\" data-large-height=\"862\" data-preview-width=\"140\" data-preview-height=\"160\" data-large=\"http://image.xmcdn.com/group21/M01/4D/E2/wKgJKFiT8fSxlPKnAAFItVf_pXc85.jpeg?op_type=4&device_type=ios&upload_type=attachment&name=mobile_large\" data-preview=\"http://image.xmcdn.com/group21/M01/4D/E2/wKgJKFiT8fSxlPKnAAFItVf_pXc85.jpeg?op_type=4&device_type=ios&upload_type=attachment&name=mobile_small\" data-origin=\"http://fdfs.xmcdn.com/group21/M01/4D/E2/wKgJKFiT8fSxlPKnAAFItVf_pXc85.jpeg\" /></span></p><span><br /></span><p style=\"font-size:16px;color:#333333;line-height:30px;word-break:break-all;font-family:Helvetica, Arial, sans-serif;font-weight:normal\" data-flag=\"normal\"><span><span><b>古典音乐很难吗？其实巴赫、贝多芬、莫扎特、肖邦、柴可夫斯基……就在你的身边，只要你打开那扇尘封已久的窗。</b></span></span></p>",
                new HtmlHttpImageGetter(htmlTextView,null,true));
//        htmlTextView.setHtml("<h1 style=\"font-size: 32px; font-weight: bold; border-bottom: 2px solid rgb(204, 204, 204); padding: 0px 4px 0px 0px; text-align: left; margin: 0px 0px 10px;\">\n" +
//                        "    <span style=\"font-size: 24px;\">吕蒙</span><br/>\n" +
//                        "</h1>\n" +
//                        "<p>\n" +
//                        "    自幼学习钢琴演奏，12岁以优异成绩考入中央音乐学院，师从杨峻教授。致力于少儿音乐的教育工作已有十余年，并创立DoFaLa快乐音乐堂，在长期的教学实践中积累了丰富的教学经验，得到众多家长的认可及孩子们的喜爱。\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    <br/>\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    <br/>\n" +
//                        "</p>\n" +
//                        "<h1 style=\"font-size: 32px; font-weight: bold; border-bottom: 2px solid rgb(204, 204, 204); padding: 0px 4px 0px 0px; text-align: left; margin: 0px 0px 10px;\">\n" +
//                        "    <span style=\"font-size: 24px;\">快乐音乐堂（大标题）</span>\n" +
//                        "</h1>\n" +
//                        "<p>\n" +
//                        "    “DoFaLa快乐音乐堂”是一套针对家长如何培养幼儿学习音乐的教材。在孩子初次进入音乐世界的时候，家长该如何引导孩子对音乐感兴趣，克服学习音乐中的困难，并使孩子在音乐中获得快乐。\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    <br/>\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    孩子刚开始接触音乐，大部分家长都无从下手，你是否有以下困惑？\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    <br/>\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    该给孩子听些什么样的音乐，又该怎样引导孩子欣赏音乐呢？&nbsp;\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    我是个不懂音乐的家长，如何帮助孩子学习音乐呢？ &nbsp;\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    如何规划每日的练琴时间？&nbsp;\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    孩子在认真练琴吗？\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    如何保持学习音乐的兴趣？\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    当孩子为你演奏一首乐曲后，如何评价孩子的音乐演奏？\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    孩子学习乐器是否要考级？\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    孩子学习音乐是否需要参加比赛？\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    我的孩子是否应能走音乐专业道路？\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    ……\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    你需要的答案，在DoFaLa快乐音乐堂中都可以找到！\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    孩子感觉“学音乐”不再那么枯燥，家长不必那样苦恼暴躁。\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    其实音乐带给你的家庭不仅仅只有美妙的乐章，还有家长和孩子亲密无间的爱。\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    <br/>\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    <br/>\n" +
//                        "</p>\n" +
//                        "<h1 style=\"font-size: 32px; font-weight: bold; border-bottom: 2px solid rgb(204, 204, 204); padding: 0px 4px 0px 0px; text-align: left; margin: 0px 0px 10px;\">\n" +
//                        "    <span style=\"font-size: 24px;\">购买须知</span><span class=\"Apple-tab-span\" style=\"white-space:pre\"></span>\n" +
//                        "</h1>\n" +
//                        "<p>\n" +
//                        "    用户购买该栏目后，将获得《快乐音乐堂》全部课程内容。\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    包括音频、图片、文稿。\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    所有栏目内容将随制作进度逐步上线，购买用户将获得完整栏目内容，无需重复购买或更新客户端。\n" +
//                        "</p>\n" +
//                        "<p>\n" +
//                        "    <br/>\n" +
//                        "</p>\n",
//                new HtmlHttpImageGetter(htmlTextView,null,true));
        initWebView();
    }
    private void initWebView() {
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setAppCacheEnabled(true);

//        webView.loadData(mIntro, "text/html", "UTF-8"); // 加载定义的代码，并设定编码格式和字符集
        webView.loadData(mIntro, "text/html; charset=UTF-8", null);
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            imgReset();//重置webview中img标签的图片大小
            // html加载完成之后，添加监听图片的点击js函数
//            addImageClickListner();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    /**
     * 对图片进行重置大小，宽度就是手机屏幕宽度，高度根据宽度比便自动缩放
     **/
    private void imgReset() {
        webView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                "    img.style.maxWidth = '100%'; img.style.height = 'auto';  " +
                "}" +
                "})()");
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_person_setting;
    }

    @Override
    public Object newP() {
        return null;
    }
    @OnClick({R.id.backlin,R.id.btn_confirm})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.backlin:
                finish();
                break;
            case R.id.btn_confirm:
//                onclickConfim();
                logout((String) SPUtil.get(context, Constants.MOKEYS_A,""));
                break;
            default:
                break;
        }
    }
    public void logout(String token) {
        Api.getMokeysLogoutService().getLoginData(token)
                .compose(XApi.<MokeysListModel<String>>getApiTransformer())
                .compose(XApi.<MokeysListModel<String>>getScheduler())
                .compose(this.<MokeysListModel<String>>bindToLifecycle())
                .subscribe(new ApiSubscriber<MokeysListModel<String>>() {
                    @Override
                    protected void onFail(NetError error) {
                    }

                    @Override
                    public void onNext(MokeysListModel<String> gankResults) {
                        if(gankResults.getCode()==0 && gankResults.getMsg().equals("success")) {
//                            Log.d("yuhh", gankResults.getData().get(0).getVerification_code());
//                            getvDelegate().toastShort("验证码为：" + gankResults.getData().get(0).getVerification_code());
                            SPUtil.clear(context);
                            finish();
                        }else{
                            getvDelegate().toastShort(gankResults.getMsg());
                        }
                    }
                });
    }
}
