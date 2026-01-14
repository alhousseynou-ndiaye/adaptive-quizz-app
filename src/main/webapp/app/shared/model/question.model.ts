import { ISubject } from 'app/shared/model/subject.model';
import { IQuestionType } from 'app/shared/model/question-type.model';

export interface IQuestion {
  id?: number;
  prompt?: string;
  difficulty?: number;
  explanation?: string | null;
  active?: boolean;
  choiceA?: string | null;
  choiceB?: string | null;
  choiceC?: string | null;
  choiceD?: string | null;
  correctChoice?: string | null;
  correctText?: string | null;
  subject?: ISubject | null;
  type?: IQuestionType | null;
}

export const defaultValue: Readonly<IQuestion> = {
  active: false,
};
