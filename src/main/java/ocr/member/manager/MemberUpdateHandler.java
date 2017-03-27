package ocr.member.manager;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.UpdateOptions;
import otocloud.common.ActionURI;
import otocloud.framework.app.function.ActionDescriptor;
import otocloud.framework.app.function.ActionHandlerImpl;
import otocloud.framework.app.function.AppActivityImpl;
import otocloud.framework.core.HandlerDescriptor;
import otocloud.framework.core.OtoCloudBusMessage;

/**
 * 会员积分更新，根据会员卡号，更新会员积分，消费次数，消费金额等服务
 * 传入参数：
 * {
 * 		no	会员卡号,
 * 		totalBuy	消费次数
 * 		totalBuyGoods	消费数量
 * 		totalBuyAmt	消费金额
 * 		recentBouns	当前积分
 *		balance	当前余额
 * 		availableNum	可用次数
  *}
 * @date 2016年11月26日
 * @author
 */
public class MemberUpdateHandler extends ActionHandlerImpl<JsonObject> {

	public static final String ADDRESS = "updateByMemberNO";

	public MemberUpdateHandler(AppActivityImpl appActivity) {
		super(appActivity);
		// TODO Auto-generated constructor stub
	}

	// 此action的入口地址
	@Override
	public String getEventAddress() {
		// TODO Auto-generated method stub
		return ADDRESS;
	}

	@Override
	public void handle(OtoCloudBusMessage<JsonObject> msg) {

		JsonObject so = msg.body();

		// 设置存在更新，不存在添加
		MongoClient mongoClient = getCurrentDataSource().getMongoClient();
		mongoClient.updateCollectionWithOptions(appActivity.getDBTableName(appActivity.getBizObjectType()),
				getQueryConditon4Update(so), getSetConditon4Update(so), new UpdateOptions().setMulti(true), res -> {
					if (res.succeeded()) {
						JsonObject settingInfo = new JsonObject();
						String queryCount = " matched doc: " + res.result().getDocMatched() + "    ";
						String modifyCount = " modified doc: " + res.result().getDocModified();
						settingInfo.put("result ", queryCount + modifyCount);
						msg.reply(settingInfo);
					} else {
						Throwable errThrowable = res.cause();
						String errMsgString = errThrowable.getMessage();
						appActivity.getLogger().error(errMsgString, errThrowable);
						msg.fail(100, errMsgString);
					}
				});

	}

	private JsonObject getQueryConditon4Update(JsonObject so) {
		JsonObject query = new JsonObject();
		// 会员卡号
		query.put("no", so.getString("no"));
		return query;
	}

	private JsonObject getSetConditon4Update(JsonObject so) {
		JsonObject boData = new JsonObject();

		boData.put("totalBuy", so.getInteger("totalBuy") + 1); // 消费次数加一
		boData.put("totalBuyGoods", so.getDouble("totalBuyGoods")); // 消费数量
		boData.put("totalBuyAmt", so.getDouble("totalBuyAmt")); // 消费金额
		boData.put("recentBouns", so.getDouble("recentBouns")); // 当前积分
		boData.put("balance", so.getDouble("balance")); // 当前余额
		boData.put("lastBuyAmt", so.getDouble("lastBuyAmt")); // 上次消费金额
		JsonObject update = new JsonObject();
		update.put("$set", boData);
		return update;
	}

	/**
	 * 此action的自描述元数据
	 */
	@Override
	public ActionDescriptor getActionDesc() {

		ActionDescriptor actionDescriptor = super.getActionDesc();
		HandlerDescriptor handlerDescriptor = actionDescriptor.getHandlerDescriptor();

		ActionURI uri = new ActionURI(ADDRESS, HttpMethod.POST);
		handlerDescriptor.setRestApiURI(uri);

		return actionDescriptor;
	}

}
