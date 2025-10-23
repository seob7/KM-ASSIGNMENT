export const validateEmail = (email) => {
  if (!email) {
    return { isValid: false, message: "이메일은 필수 입력 사항입니다." };
  }

  if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
    return { isValid: false, message: "이메일 형식이 올바르지 않습니다." };
  }

  if (email.length > 200) {
    return { isValid: false, message: "이메일은 200자 이내여야 합니다." };
  }

  return { isValid: true, message: "" };
};

export const validatePassword = (password) => {
  if (!password) {
    return { isValid: false, message: "비밀번호는 필수 입력 사항입니다." };
  }

  if (password.length < 5 || password.length > 20) {
    return {
      isValid: false,
      message: "비밀번호는 5자 이상 20자 이하여야 합니다.",
    };
  }

  if (!/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]*$/.test(password)) {
    return {
      isValid: false,
      message: "비밀번호는 영문과 숫자를 포함해야 합니다.",
    };
  }

  return { isValid: true, message: "" };
};

export const validateNickname = (nickname) => {
  if (!nickname) {
    return { isValid: false, message: "닉네임은 필수입니다." };
  }

  if (nickname.length < 2 || nickname.length > 30) {
    return {
      isValid: false,
      message: "닉네임은 2자 이상 30자 이하여야 합니다.",
    };
  }

  return { isValid: true, message: "" };
};

export const validatePasswordConfirm = (password, confirmPassword) => {
  if (password !== confirmPassword) {
    return { isValid: false, message: "비밀번호가 일치하지 않습니다." };
  }

  return { isValid: true, message: "" };
};

export const validateLoginForm = (formData) => {
  const emailValidation = validateEmail(formData.email);
  if (!emailValidation.isValid) {
    return emailValidation;
  }

  const passwordValidation = validatePassword(formData.password);
  if (!passwordValidation.isValid) {
    return passwordValidation;
  }

  return { isValid: true, message: "" };
};

export const validateRegisterForm = (formData) => {
  const emailValidation = validateEmail(formData.email);
  if (!emailValidation.isValid) {
    return emailValidation;
  }

  const nicknameValidation = validateNickname(formData.nickname);
  if (!nicknameValidation.isValid) {
    return nicknameValidation;
  }

  const passwordValidation = validatePassword(formData.password);
  if (!passwordValidation.isValid) {
    return passwordValidation;
  }

  const passwordConfirmValidation = validatePasswordConfirm(
    formData.password,
    formData.confirmPassword
  );
  if (!passwordConfirmValidation.isValid) {
    return passwordConfirmValidation;
  }

  return { isValid: true, message: "" };
};
