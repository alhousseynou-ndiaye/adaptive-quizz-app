import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IQuestion } from 'app/shared/model/question.model';

export interface IAnswer {
  id?: number;
  answeredAt?: dayjs.Dayjs;
  isCorrect?: boolean;
  timeSpentMs?: number | null;
  selfReportedRecall?: number;
  difficultyAtAnswer?: number;
  selectedChoice?: string | null;
  freeTextAnswer?: string | null;
  user?: IUser | null;
  question?: IQuestion | null;
}

export const defaultValue: Readonly<IAnswer> = {
  isCorrect: false,
};
