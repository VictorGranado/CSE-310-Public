import { User } from './user.model';
import { Task } from './task.model';

export interface Project {
  projectId: number;
  title: string;
  description: string;
  projectType: 'Individual' | 'Group';
  owner: User;
  startDate: Date;
  endDate?: Date;
  tasks: Task[];
}
