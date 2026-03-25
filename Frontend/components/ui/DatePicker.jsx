import { TextField } from "@mui/material";
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import dayjs from "dayjs";

export default function DatePickerUI({
    label,
    value,
    onChange,
    format = "DD/MM/YYYY", // yyyy-MM-dd
    ...props
}) {
    return (
        <LocalizationProvider dateAdapter={AdapterDayjs}>
            <DatePicker
                label={label}
                value={value && dayjs(value).isValid() ? dayjs(value) : null}
                onChange={(newValue) => {
                    onChange(newValue ? newValue.format("YYYY-MM-DD") : null);
                }}
                format={format}
                // slots={{
                //     textField: TextField
                // }}
                slotProps={{
                    textField: {
                        variant: "standard",
                        fullWidth: true,
                        margin: "normal"
                    }
                }}
                {...props}
            />
        </LocalizationProvider>
    );
}