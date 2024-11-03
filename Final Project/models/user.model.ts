export interface User {
    userId: number;
    name: string;
    email: string;
    role: 'Admin' | 'Supervisor' | 'Team Member' | 'Client';
    skills: Skill[];
  }
  