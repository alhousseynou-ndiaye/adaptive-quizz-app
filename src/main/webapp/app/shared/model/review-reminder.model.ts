import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IQuestion } from 'app/shared/model/question.model';
import { ReminderStatus } from 'app/shared/model/enumerations/reminder-status.model';
import { ReminderKind } from 'app/shared/model/enumerations/reminder-kind.model';

export interface IReviewReminder {
  id?: number;
  dueAt?: dayjs.Dayjs;
  status?: keyof typeof ReminderStatus;
  kind?: keyof typeof ReminderKind;
  createdAt?: dayjs.Dayjs;
  doneAt?: dayjs.Dayjs | null;
  user?: IUser | null;
  question?: IQuestion | null;
}

export const defaultValue: Readonly<IReviewReminder> = {};
