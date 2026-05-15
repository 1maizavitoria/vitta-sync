import { TextField } from "@mui/material";
import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import dayjs from "dayjs";

export default function DatePickerUI({
    label,
    value,
    onChange,
    error,
    dateLimit,
    format = "DD/MM/YYYY", // yyyy-MM-dd
    ...props
}) {
    return (
        <LocalizationProvider dateAdapter={AdapterDayjs}>
            <DatePicker
                label={label}
                value={value && dayjs(value).isValid() ? dayjs(value) : null}
                maxDate={dateLimit || dayjs()}
                onChange={(newValue) => {
                    onChange(newValue ? newValue.format("YYYY-MM-DD") : null);
                }}
                format={format}
                slotProps={{
                    textField: {
                        variant: "outlined",
                        fullWidth: true,
                        margin: "normal",
                        error: error,
                        size: "small",
                        sx: {
                            '& .MuiInputLabel-root': {
                                transform: 'translate(14px, 9px) scale(1)',
                            },

                            '& .MuiInputLabel-root.Mui-focused, & .MuiInputLabel-root.MuiFormLabel-filled': {
                                transform: 'translate(10px, -20px) scale(0.85)',
                                padding: '0 4px',
                            },

                            '& .MuiOutlinedInput-root, & .MuiPickersOutlinedInput-root': {
                                position: 'relative',
                                borderRadius: '30px',
                                backgroundColor: '#e0e0e0',
                                transition: 'all 0.5s ease',

                                '& input': {
                                    padding: '10px 14px',
                                },

                                '& fieldset': {
                                    border: '1px solid transparent',
                                },

                                '&:hover::before, &.Mui-focused::before': {
                                    content: '""',
                                    position: 'absolute',
                                    inset: 0,
                                    borderRadius: '30px',
                                    padding: '1.5px',
                                    background: 'linear-gradient(to right, #00b7ff, #00ff55)',

                                    WebkitMask:
                                        'linear-gradient(#fff 0 0) content-box, linear-gradient(#fff 0 0)',
                                    WebkitMaskComposite: 'xor',
                                    maskComposite: 'exclude',

                                    pointerEvents: 'none',
                                },

                                '&.Mui-focused': {
                                    backgroundColor: '#ffffff',
                                    boxShadow: 'inset 0px 2px 10px rgba(0,0,0,0.2)',
                                },

                                '&:hover': {
                                    backgroundColor: '#eeeeee',
                                    boxShadow: '0px 5px 15px -2px #4e4e4e',
                                }
                            }
                        }

                    }
                }
                }
                {...props}
            />
        </LocalizationProvider>
    );
}