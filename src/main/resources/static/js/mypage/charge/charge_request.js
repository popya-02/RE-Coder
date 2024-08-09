const API_KEY = payment.apikey;
const SHOP_ID = payment.shopID;
const CHANNEL_KEY = payment.channelKey;


let coinPriceBtnEle = document.getElementsByName('coin-price');
let resultCoinCnt = 0;
let resultCoinPrice = 0;
let userNo = 1; // 임시

for (const ele of Array.from(coinPriceBtnEle)) {
    ele.addEventListener("click", coinResponseTest);
}

function coinResponseTest(event) {
    event.preventDefault(); // 링크의 기본 동작을 막습니다.
    resultCoinCnt = "하트 코인 "+event.target.getAttribute('data-value')+"개";
    resultCoinPrice = event.target.getAttribute('data-value')*100;
    console.log(resultCoinCnt); // data-value 값 출력
    console.log(resultCoinPrice); // data-value 값 출력

    let paymentNo = 0;

    $.ajax({
        type: "GET",	// method
        url: "/charge/sequence",
        success: function(data) {
            paymentNo = data;
            console.log(paymentNo); // data-value 값 출력
            requestPayment(String(paymentNo))
        },
        error: function() {
            alert("error");
        }
    })

}




async function requestPayment(paymentNo) {

    try {
        // 먼저 결제 대기 상태를 서버에 전송
        const response = await fetch("/charge/pending", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                paymentNo: paymentNo,
                paymentUserNo: userNo,
                paymentAmount: resultCoinPrice,
                paymentProduct: resultCoinCnt,
            }),
        });
        const data = await response.text(); // 또는 response.json() if expecting JSON response

        if (data === 'success') {
            // 결제 요청 처리
            // const paymentResponse = await PortOne.requestPayment({
            //     storeId: SHOP_ID,
            //     channelKey: CHANNEL_KEY,
            //     paymentId: paymentNo,
            //     orderName: resultCoinCnt,
            //     totalAmount: resultCoinPrice,
            //     currency: "KRW",
            //     payMethod: "CARD",
            //     customer: {
            //         fullName: "johndoe",
            //         phoneNumber: "123-456-7890",
            //         email: "john.doe@example.com",
            //     },
            // });
            //
            // if (paymentResponse.code != null) {
            //     // 오류 발생
            //     return alert(paymentResponse.message);
            // }
            // 결제 완료 상태를 서버에 전송
            const testList = {
                    storeId: SHOP_ID,
                    channelKey: CHANNEL_KEY,
                    paymentId: paymentNo,
                    orderName: resultCoinCnt,
                    totalAmount: resultCoinPrice,
                    currency: "KRW",
                    payMethod: "CARD",
                    customer: {
                        fullName: "johndoe",
                        phoneNumber: "123-456-7890",
                        email: "john.doe@example.com",
                    },
                }

            await fetch("/charge/complete", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    // paymentNo: paymentResponse.paymentId
                    paymentNo: "1"
                }),
            });
        }
    } catch (error) {
        console.error(error);
        alert("An error occurred : " + error);
    }
}
