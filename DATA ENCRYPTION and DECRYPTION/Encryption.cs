using System.Security.Cryptography;
using System.Text;

namespace FileEncryptionApp
{
    public abstract class Encryption
    {
        protected byte[] GenerateKeyFromPassword(string password)
        {
            using (var sha256 = SHA256.Create())
            {
                return sha256.ComputeHash(Encoding.UTF8.GetBytes(password));
            }
        }

        public abstract void Encrypt(string filePath, string password);
        public abstract void Decrypt(string filePath, string password);
    }
}
