import {AbstractControl} from "@angular/forms";

export function personalCodeValidator(control: AbstractControl): { [key: string]: boolean } | null {
  const personalCode = control.value;

  if (personalCode && personalCode.length === 11) {
    let genderCorrect: boolean = isGenderNumberCorrect(personalCode);
    let dayCorrect: boolean = isDayNumberCorrect(personalCode);
    let monthCorrect: boolean = isMonthNumberCorrect(personalCode);
    let yearCorrect: boolean = isYearNumberCorrect(personalCode);
    let controlNumberCorrect: boolean = isControlNumberCorrect(personalCode);

    if (genderCorrect && dayCorrect && monthCorrect && yearCorrect && controlNumberCorrect) {
      return null;
    }
  }

  return { 'invalidPersonalCode': true };
}

function isGenderNumberCorrect(personalCode: string): boolean {
  const correctNumbers: number[] = [1, 2, 3, 4, 5, 6];
  const genderNumber: number = +personalCode[0];
  return correctNumbers.some(n => n === genderNumber);
}

function isYearNumberCorrect(personalCode: string): boolean {
  const yearNumber: number = +personalCode.substring(1, 3);
  const maxYear: number = 99;
  return 1 <= yearNumber && yearNumber <= maxYear;
}

function isMonthNumberCorrect(personalCode: string): boolean {
  const monthNumber: number = +personalCode.substring(3, 5);
  const maxMonth: number = 12;
  return 1 <= monthNumber && monthNumber <= maxMonth;
}

function isDayNumberCorrect(personalCode: string): boolean {
  const dayNumber: number = +personalCode.substring(5, 7);
  const monthNumber: number = +personalCode.substring(3, 5);

  const monthNumbers31: number[] = [1, 3, 5, 7, 8, 10, 12];
  const monthNumbers30: number[] = [4, 6, 9, 11];

  const februaryDay: number = 28;
  const februaryLeapDay: number = 29;
  const maxDay1: number = 31;
  const maxDay2: number = 30;

  if (monthNumber == 2 && dayNumber <= februaryDay) {
    return true;
  } else if (isLeapYear(getFullYear(personalCode)) && monthNumber == 2 && dayNumber == februaryLeapDay) {
    return true;
  } else if (monthNumbers31.some(m => m === monthNumber) && dayNumber <= maxDay1) {
    return true;
  } else {
    return monthNumbers30.some(m => m === monthNumber) && dayNumber <= maxDay2;
  }
}

function isLeapYear(fullYear: number) {
  const leapYearNumber: number = 400;
  if (fullYear % leapYearNumber == 0) {
    return true;
  } else {
    return fullYear % 4 == 0 && fullYear % 100 != 0;
  }
}

function getFullYear(personalCode: string) {
  const year: string = personalCode.substring(1, 3);
  const genderNumber: number = +personalCode.substring(0, 1);

  if (genderNumber == 1 || genderNumber == 2) {
    return +("18" + year);
  } else if (genderNumber == 3 || genderNumber == 4) {
    return +("19" + year);
  } else {
    return +("20" + year);
  }

}

function isControlNumberCorrect(personalCode: string): boolean {
  const controlNumber: number = +personalCode.charAt(10);
  const idCodeLength: number = 11;

  let sum: number = getIdCodeSum([1, 2, 3, 4, 5, 6, 7, 8, 9, 1], personalCode);
  let remainder: number = sum % idCodeLength;
  // First algorithm
  if (remainder == controlNumber) {
    return true;
  } else if (remainder == 10) {
    // Second algorithm
    sum = getIdCodeSum([3, 4, 5, 6, 7, 8, 9, 1, 2, 3], personalCode);
    remainder = sum % idCodeLength;
    if (remainder == controlNumber) {
      return true;
    } else return remainder == 10;
  } else {
    return false;
  }

}

function getIdCodeSum(scale: number[], personalCode: string) {
  let sum: number = 0;
  for (let i = 0; i < scale.length; i++) {
    sum += scale[i] * +personalCode.charAt(i);
  }
  return sum;
}


