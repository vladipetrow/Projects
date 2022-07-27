import { useState } from "react";
import "./style.css";

export const BTCPay = () => {
    return (
        
 <form method="POST" action="https://btcpay.coincharge.io/api/v1/invoices" className="btcpay-form btcpay-form--block">
 <input type="hidden" name="storeId" value="7Fo9zM7sTVxvm9hhvoHTSeH7cGjoQjhSV97vqEmR5buP" />
 <input type="hidden" name="price" value="2" />
 <input type="hidden" name="currency" value="USD" />
 <input type="image" className="submit" name="submit" src="https://btcpay.coincharge.io/img/paybutton/pay.svg" sx="width:209px" alt="Pay with BTCPay Server, a Self-Hosted Bitcoin Payment Processor"/>
</form>

    )
}