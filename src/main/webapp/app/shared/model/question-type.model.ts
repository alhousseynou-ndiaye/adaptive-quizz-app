import { QuestionTypeCode } from 'app/shared/model/enumerations/question-type-code.model';

export interface IQuestionType {
  id?: number;
  code?: keyof typeof QuestionTypeCode;
  label?: string;
}

export const defaultValue: Readonly<IQuestionType> = {};
