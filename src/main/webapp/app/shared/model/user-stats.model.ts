import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ISubject } from 'app/shared/model/subject.model';

export interface IUserStats {
  id?: number;
  currentDifficulty?: number;
  totalAnswered?: number;
  totalCorrect?: number;
  streakDays?: number;
  lastActiveAt?: dayjs.Dayjs | null;
  user?: IUser | null;
  subject?: ISubject | null;
}

export const defaultValue: Readonly<IUserStats> = {};
