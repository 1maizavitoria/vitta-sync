import dayjs from "dayjs";

export function getDateLimit(userType) {
    if (userType === "responsavel" || userType === "saude") {
        return dayjs().subtract(18, "year");
    }
    return dayjs();
};

export function isUnder18(date) {
    return dayjs().diff(dayjs(date), "year") < 18;
};