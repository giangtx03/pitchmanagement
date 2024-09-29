export const formatDate = (dateString: string): string => {
  const date = new Date(dateString);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");
  const seconds = String(date.getSeconds()).padStart(2, "0");

  return `${hours}:${minutes} Ngày ${day}-${month}-${year} `;
};

export const formatTime = (timeString: string): string => {
  return timeString.split(":").slice(0, 2).join(":");
};

export const convertBookingStatus = (bookingStatus: string): string => {
  switch (bookingStatus) {
    case "PENDING":
      return "Đang xử lý";
    case "DEPOSIT_PAID":
      return "Đã cọc";
    case "CONFIRMED":
      return "Đã xác nhận";
    case "COMPLETED":
      return "Đã hoàn thành";
    case "CANCELLED":
      return "Đã hủy";
    case "REQUEST_CANCELLATION":
      return "Đang xử lý hủy";
    default:
      return "";
  }
};
