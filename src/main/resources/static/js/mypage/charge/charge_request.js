var coinPriceBtnEle = document.getElementsByName('coin-price');

for (const ele of Array.from(coinPriceBtnEle)) {
    ele.addEventListener("click", coinResponse);
}




const coinResponse = await PortOne.requestPayment({
    // Store ID 설정
    storeId: "",
    // 채널 키 설정
    channelKey: "" ,
    paymentId: `payment-${crypto.randomUUID()}`,
    orderName: "",
    totalAmount: 1000,
    currency: "CURRENCY_KRW",
    payMethod: "CARD",
});