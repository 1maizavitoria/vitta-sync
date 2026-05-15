export const hasMinLength = (password) => password.length >= 8;
export const hasUppercase = (password) => /[A-Z]/.test(password);
export const hasLowercase = (password) => /[a-z]/.test(password);
export const hasNumber = (password) => /\d/.test(password);
export const hasSpecialChar = (password) => /[^A-Za-z\d]/.test(password);

export function validatePassword(password) {
    const result = {
        minLength: hasMinLength(password),
        upperCase: hasUppercase(password),
        lowerCase: hasLowercase(password),
        number: hasNumber(password),
        specialChar: hasSpecialChar(password)
    };

    return {
        ...result,
        isValid: Object.values(result).every(Boolean)
    };
}