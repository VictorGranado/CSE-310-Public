using System;
using System.IO;
using System.Security.Cryptography;

namespace FileEncryptionApp
{
    public class FileDecryptor : Encryption
    {
        public override void Encrypt(string filePath, string password)
        {
            Console.WriteLine("This function is not supported in FileDecryptor.");
        }

        public override void Decrypt(string filePath, string password)
        {
            byte[] key = GenerateKeyFromPassword(password);

            using (FileStream fs = new FileStream(filePath, FileMode.Open))
            {
                byte[] iv = new byte[16];
                fs.Read(iv, 0, iv.Length);

                using (Aes aes = Aes.Create())
                {
                    aes.Key = key;
                    aes.IV = iv;

                    using (CryptoStream cs = new CryptoStream(fs, aes.CreateDecryptor(), CryptoStreamMode.Read))
                    using (FileStream fsOutput = new FileStream(filePath.Replace(".enc", ""), FileMode.Create))
                    {
                        cs.CopyTo(fsOutput);
                    }
                }
            }
            Console.WriteLine("File decrypted successfully.");
        }
    }
}
