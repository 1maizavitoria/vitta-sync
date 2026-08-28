import { DatePicker } from '@mui/x-date-pickers/DatePicker';
import { LocalizationProvider } from '@mui/x-date-pickers/LocalizationProvider';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import { useTheme } from '@mui/material/styles';
import dayjs from "dayjs";

import { useI18n } from "../../src/i18n";

export default function DatePickerUI({
    label,
    value,
    onChange,
    error,
    dateLimit,
    format,
    ...props
}) {
    const theme = useTheme();
    const isDark = theme.palette.mode === "dark";
    const { dateInputFormat } = useI18n();
    const { sx, ...datePickerProps } = props;

    return (
        <LocalizationProvider dateAdapter={AdapterDayjs}>
            <DatePicker
                label={label}
                value={value && dayjs(value).isValid() ? dayjs(value) : null}
                maxDate={dateLimit || dayjs()}
                onChange={(newValue) => {
                    onChange(newValue ? newValue.format("YYYY-MM-DD") : null);
                }}
                format={format || dateInputFormat}
                slotProps={{
                    textField: {
                        variant: "outlined",
                        fullWidth: true,
                        margin: "normal",
                        error: error,
                        size: "small",
                        sx: {
                            width: "100%",
                            ...sx,
                            '& .MuiInputLabel-root': {
                                transform: 'translate(14px, 9px) scale(1)',
                                color: 'text.secondary',
                            },

                            '& .MuiInputLabel-root.Mui-focused, & .MuiInputLabel-root.MuiFormLabel-filled': {
                                transform: 'translate(10px, -18px) scale(0.85)',
                                backgroundColor: 'background.paper',
                                color: 'primary.main',
                                padding: '0 4px',
                            },

                            '& .MuiOutlinedInput-root, & .MuiPickersOutlinedInput-root': {
                                position: 'relative',
                                borderRadius: 2,
                                backgroundColor: isDark ? 'rgba(220, 252, 231, 0.06)' : 'rgba(22, 163, 74, 0.06)',
                                transition: 'all 0.2s ease',

                                '& input': {
                                    padding: '10px 14px',
                                },

                                '& fieldset': {
                                    borderColor: theme.vitta.border,
                                },

                                '&.Mui-focused': {
                                    backgroundColor: 'background.paper',
                                    boxShadow: isDark ? '0 0 0 4px rgba(34, 197, 94, 0.12)' : '0 0 0 4px rgba(22, 163, 74, 0.12)',

                                    '& fieldset': {
                                        borderColor: 'primary.main',
                                    }
                                },

                                '&:hover': {
                                    backgroundColor: 'background.paper',

                                    '& fieldset': {
                                        borderColor: 'primary.main',
                                    }
                                }
                            }
                        }
                    }
                }}
                {...datePickerProps}
            />
        </LocalizationProvider>
    );
}
