package com.liemi.seashellmallclient.data.api;

import com.liemi.seashellmallclient.data.entity.article.ArticleClassEntity;
import com.liemi.seashellmallclient.data.entity.article.ArticleCommentEntity;
import com.liemi.seashellmallclient.data.entity.article.ArticleListEntity;
import com.liemi.seashellmallclient.data.entity.article.MyCommentEntity;
import com.liemi.seashellmallclient.data.entity.article.ShopCartArticleTimeEntity;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.PageEntity;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Bingo on 2019/1/2.
 */

public interface ArticleApi {
    /**
     * 学院咨询分类信息
     */
    @FormUrlEncoded
    @POST("information/information-api/theme-index")
    Observable<BaseData<PageEntity<ArticleClassEntity>>> getArticleClasses(@Field("pid") String pid);

    /**
     * 学院咨询列表
     * thid	是	int	一级分类主键 13预测中心；14新手指引；15大咖讲堂；16最新资讯
     */
    @FormUrlEncoded
    @POST("information/information-api/index")
    Observable<BaseData<PageEntity<ArticleListEntity>>> getArticleList(@Field("start_page") int startPage,
                                                                       @Field("pages") int pages,
                                                                       @Field("thid") String classId);

    /**
     * 阅读/点赞/收藏资讯API
     * inid	int
     资讯主键

     type	int
     操作类型 1：阅读 2：点赞 3：收藏
     */
    @FormUrlEncoded
    @POST("information/information-api/add-collect")
    Observable<BaseData> articleAddLikeOrCollect(@Field("inid") String articleId,
                                                 @Field("type") int type);

    /**
     * 咨询评论列表
     */
    @FormUrlEncoded
    @POST("information/information-api/comment-index")
    Observable<BaseData<PageEntity<ArticleCommentEntity>>> getArticleCommentList(@Field("start_page") int startPage,
                                                                                 @Field("pages") int pages,
                                                                                 @Field("inid") String classId);

    /**
     * 咨询发表评论
     */
    @FormUrlEncoded
    @POST("information/information-api/add-comment")
    Observable<BaseData> doArticleComment(@Field("inid") String classId,
                                          @Field("content") String content,
                                          @Field("scenario") String scenario);

    /**
     * 我的咨询评论列表
     */
    @FormUrlEncoded
    @POST("information/information-api/user-comment")
    Observable<BaseData<PageEntity<MyCommentEntity>>> getMyArticleCommentList(@Field("start_page") int startPage,
                                                                              @Field("pages") int pages);

    /*
     * 举报评论
     * */
    @FormUrlEncoded
    @POST("information/information-api/report")
    Observable<BaseData> activityReport(@Field("act_com_id") String act_com_id,@Field("type") String type);
     /*
     *举报评论
     * */
    @FormUrlEncoded
    @POST("information/information-api/fabulous")
    Observable<BaseData> activityZan(@Field("id") String id,@Field("type") String type);

    /*
    * 删除我的评论
    * */
    @FormUrlEncoded
    @POST("information/information-api/delete-comment")
    Observable<BaseData> doDelComment(@Field("id") String id);


    /**
     * 获取对应类型的消息列表
     * 公告类型（1：用户公告 ；3商学院；4资产消息
     */
    @FormUrlEncoded
    @POST("notice/notice-api/get-message-from-type")
    Observable<BaseData<PageEntity<ShopCartArticleTimeEntity>>> getShopCartArticle(@Field("start_page") int startPage,
                                                                                   @Field("pages") int pages,
                                                                                   @Field("type_arr[]") String[] type);

    /**
     * 设置消息已读
     * 公告类型（1：用户公告 ；3商学院；4资产消息
     */
    @FormUrlEncoded
    @POST("notice/notice-api/set-type-read-all")
    Observable<BaseData> setReadAll(@Field("type") String type);
}
