import "./style.css";

export const BTCPay = ({ storeId, price, currency }) => {
    return (
        <form method="POST" action="https://btcpay.coincharge.io/api/v1/invoices" className="btcpay-form">
            <input type="hidden" name="storeId" value={storeId} />
            <input type="hidden" name="price" value={price} />
            <input type="hidden" name="currency" value={currency} />
            <input type="image" className="submit" name="submit" src="https://btcpay.coincharge.io/img/paybutton/pay.svg" alt="Pay with BTCPay" />
        </form>
    );
};
